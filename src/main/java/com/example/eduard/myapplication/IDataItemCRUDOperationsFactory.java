package com.example.eduard.myapplication;

public interface IDataItemCRUDOperationsFactory {

	public IDataItemCRUDOperations getDataItemCRUDOperationsImplForName(String implName);
	
}