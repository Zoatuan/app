package com.example.eduard.myapplication;

import java.util.List;

public interface IDataItemCRUDOperations {

	/*
	 * the operations
	 */

	public DataItem createDataItem(DataItem item);

	public List<DataItem> readAllDataItems();

	public DataItem readDataItem(int dateItemId);

	public DataItem updateDataItem(int id, DataItem item);

	public boolean deleteDataItem(int dataItemId);

}
