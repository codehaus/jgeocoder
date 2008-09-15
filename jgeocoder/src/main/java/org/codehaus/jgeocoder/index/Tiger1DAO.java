package org.codehaus.jgeocoder.index;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;

public class Tiger1DAO{
	  
	  private PrimaryIndex<String, Tiger1> tiger1ById;
	  
	  public Tiger1DAO(EntityStore store) throws DatabaseException{
		  tiger1ById = store.getPrimaryIndex(String.class, Tiger1.class);
	  }
	  
	  public Tiger1 getById(String id) throws DatabaseException{
		  return tiger1ById.get(id);
	  }
	  
	  public PrimaryIndex<String, Tiger1> getTiger1ById() {
		return tiger1ById;
	}
}