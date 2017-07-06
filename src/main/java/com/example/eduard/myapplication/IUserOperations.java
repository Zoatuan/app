package com.example.eduard.myapplication;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

public interface IUserOperations {

	public boolean checkLogin(User user);

}
