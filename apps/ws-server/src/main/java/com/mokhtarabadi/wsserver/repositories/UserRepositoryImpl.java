/* (C) 2023 Mohammad Reza Mokhtarabadi <mmokhtarabadi@gmail.com> */
package com.mokhtarabadi.wsserver.repositories;

public class UserRepositoryImpl implements UserRepository {
	@Override
	public boolean isUserExists(String username) {
		return true;
	}
}
