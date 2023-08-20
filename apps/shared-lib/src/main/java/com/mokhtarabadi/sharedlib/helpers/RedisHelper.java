/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.sharedlib.helpers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Slf4j
public class RedisHelper {

	private JedisPool jedisPool;
	private final ExecutorService executorService;

	private RedisHelper() {
		executorService = Executors.newFixedThreadPool(4);
	}

	private static class SingletonHolder {
		private static final RedisHelper INSTANCE = new RedisHelper();
	}

	public static RedisHelper getInstance() {
		return SingletonHolder.INSTANCE;
	}

	public void initializeConnection(String host, int port, @Nullable String password) {
		if (password != null && !password.isEmpty()) {
			jedisPool = new JedisPool(new JedisPoolConfig(), host, port, 0, password);
		} else {
			jedisPool = new JedisPool(new JedisPoolConfig(), host, port);
		}
		log.debug("Redis initialized.");
	}

	public CompletableFuture<Void> addToSet(String key, String value) {
		return CompletableFuture.runAsync(() -> {
			try (Jedis jedis = jedisPool.getResource()) {
				jedis.sadd(key, value);
			}
		}, executorService);
	}

	public CompletableFuture<Set<String>> getSetValues(String key) {
		return CompletableFuture.supplyAsync(() -> {
			try (Jedis jedis = jedisPool.getResource()) {
				return new HashSet<>(jedis.smembers(key));
			}
		}, executorService);
	}

	public CompletableFuture<Void> removeFromSet(String key, String value) {
		return CompletableFuture.runAsync(() -> {
			try (Jedis jedis = jedisPool.getResource()) {
				jedis.srem(key, value);
			}
		}, executorService);
	}

	public CompletableFuture<Void> addToList(String key, String value) {
		return CompletableFuture.runAsync(() -> {
			try (Jedis jedis = jedisPool.getResource()) {
				jedis.lpush(key, value);
			}
		}, executorService);
	}

	public CompletableFuture<List<String>> getListValues(String key) {
		return CompletableFuture.supplyAsync(() -> {
			try (Jedis jedis = jedisPool.getResource()) {
				return jedis.lrange(key, 0, -1);
			}
		}, executorService);
	}

	public CompletableFuture<Void> removeFromList(String key, String value) {
		return CompletableFuture.runAsync(() -> {
			try (Jedis jedis = jedisPool.getResource()) {
				jedis.lrem(key, 0, value);
			}
		}, executorService);
	}

	public void closeConnection() {
		jedisPool.close();

		// gracefully shutdown the executor
		executorService.shutdown();
		try {
			executorService.awaitTermination(1, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			log.error("Failed to shutdown executor.", e);
		} finally {
			if (!executorService.isShutdown()) {
				executorService.shutdownNow();
			}
		}
	}
}