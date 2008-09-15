package org.codehaus.jgeocoder.index;

import static org.codehaus.jgeocoder.DataUtils.expandLocalityAbbrv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jgeocoder.DataUtils;
import org.codehaus.jgeocoder.index.Tiger1IndexBuilder.Street;

import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.StoreConfig;

public class Tiger1Bdb{
	
	public static void main(String[] args) throws Exception{
		Tiger1Bdb db = new Tiger1Bdb();
		db.init(new File("/usr/local/jgeocoder/data/tiger1"), false, false);
		Tiger1DAO dao = new Tiger1DAO(db.getStore());
		BufferedReader r = null;
		BufferedReader r2 = null;
		if(true) throw new RuntimeException("dont run");
		try {
			
			Map<String, String> zip2City = new HashMap<String, String>();
			r2 = new BufferedReader(new FileReader(Thread.currentThread().getContextClassLoader().getResource("org/codehaus/jgeocoder/index/ZIP_CODES.txt").getFile()));
			
			String line = null;
			int count = 0;
			while((line = r2.readLine()) != null){
				String[] all = line.split(",");
				zip2City.put(all[0].substring(1, 6), all[3].replaceAll("\"", ""));
			}
			
			
			
			
		    r = new BufferedReader(new FileReader("/home/liangj01/Desktop/street_all.txt"));
			line = null;
			count = 0;
			while((line = r.readLine()) != null){
				if(count++ > 1000){
					System.out.println(count);
				}
				
				
				
				String[] items = line.split("[|]");
				int i =0;
				
				String fedirp = StringUtils.trimToNull(items[i++]);
				String street = StringUtils.trimToNull(items[i++]);
				String fetype = StringUtils.trimToNull(items[i++]);
				String fedirs = StringUtils.trimToNull(items[i++]);
				String county = StringUtils.trimToNull(items[i++]);
				String state = StringUtils.trimToNull(items[i++]);
				String zipcode = StringUtils.trimToNull(items[i++]);
				String tlid = items[i++];
		
				Set<String> ret = new HashSet<String>();

		    	String type = expandLocalityAbbrv(fetype, false);
		    	
		    	street = expandLocalityAbbrv(street, true);
		    	
		    	String city = zip2City.get(zipcode);
		    	state = DataUtils.expandState(state);
		    	
		    	
		    	String s = new Street(street, type, county, city, state).toString();
		    	Tiger1 t1 = new Tiger1();
		    	t1.setId(String.valueOf(count));
		    	t1.setValue(s);
		    	dao.getTiger1ById().put(t1);

			}
			
		} finally{
			IOUtils.closeQuietly(r);
			IOUtils.closeQuietly(r2);
		}
	}
	
	private Environment _env = null;
	  private EntityStore _store = null;
	  public Environment getEnv() {
	    return _env;
	  }
	  public EntityStore getStore() {
	    return _store;
	  }
	  
	  public void init(File envHome, boolean readOnly, boolean transactional) throws DatabaseException{
	    
	    EnvironmentConfig config = new EnvironmentConfig();
	    config.setAllowCreate(!readOnly);
	    config.setReadOnly(readOnly);
	    config.setTransactional(transactional);
	
	    _env = new Environment(envHome, config);
	    StoreConfig config2 = new StoreConfig();
	    config2.setAllowCreate(!readOnly);
	    config2.setReadOnly(readOnly);
	    config2.setTransactional(transactional);
	    _store = new EntityStore(_env, "Tiger1EntityStore", config2);
	  }
	  
	  
	  public void shutdown() throws DatabaseException{
	    if(_store != null){
	      _store.close();
	    }
	    if(_env != null){
	      _env.close();
	    }
	  }
}