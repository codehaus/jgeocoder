/**
 * 
 */
package org.codehaus.jgeocoder

import java.util.zip.ZipFile
import org.apache.commons.lang.StringUtils

def FIPS_STATE_MAP = [:]
def FIPS_COUNTY_MAP = [:]

new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream('org/codehaus/jgeocoder/code.txt')).eachLine{
	def values = it.split(/[ ]{5}/)
	FIPS_STATE_MAP[values[0]] = values[3].replaceAll('\\W+', ' ').toUpperCase()
	FIPS_COUNTY_MAP[values[0]+values[1]] = values[2].replaceAll('\\W+', ' ').toUpperCase()
}


def ps = new PrintStream(new FileOutputStream('street_dict.txt', true))

new File('/home/liangj01/Desktop/tiger').eachFileRecurse{
  if(it.name ==~ /TGR.+\.ZIP/){
	  println it.name
	  ZipFile zip = new ZipFile(it)
	  def entries = zip.entries()
	  while(entries.hasMoreElements()){
			process(it.name, zip, entries.nextElement(), FIPS_STATE_MAP, FIPS_COUNTY_MAP, ps)
	  }
	  zip.close()
  }
}


void process(def filename, def zip, def entry, def FIPS_STATE_MAP, def FIPS_COUNTY_MAP, def ps){
	if(!entry.getName().toUpperCase().endsWith('RT1')) return
	
	def reader = new BufferedReader(new InputStreamReader(zip.getInputStream(entry)))
	def ext = entry.name.substring(entry.name.lastIndexOf('.')+1)
	if(ext == 'MET') return
	def tableName = 'tiger_'+ext.toLowerCase().substring(2)
	TigerTable table
	TigerDefinition.TIGER_TABLES.each{
	    if(it.name == tableName) table = it
	}

	def all = new HashSet()
	def code = filename.substring(3,8)
	reader.eachLine{line ->
	    if(StringUtils.isBlank(line)) return
	    def values = [:]
	    table.columns.each{ c ->
		    def val = line[c.range].trim()
		    val = StringUtils.upperCase(val) 
		    values[c.name] = val
	    }
	    
	    def state = FIPS_STATE_MAP[values['STATEL']]
	    if(StringUtils.isEmpty(state)){
	    	state = FIPS_STATE_MAP[values['STATER']]
	    }
		def county = FIPS_COUNTY_MAP[code]
		
		def zipcode = values['ZIPL']
		if(StringUtils.isEmpty(zipcode)){
			zipcode = values['ZIPR']
	    }	    
	    String street = values['FENAME']
	    def streetTokens = street.split(/\s+/)
	    def word, prev, post
	    for(int i =0; i< streetTokens.length; i++){
	    	word = streetTokens[i]
	    	prev = i>0? streetTokens[i-1] : ''
	        post = i<(streetTokens.length-1)? streetTokens[i+1] : values['FETYPE']
	        if(StringUtils.isBlank(word) || word.length() < 3) continue
	        all << "${[prev, word, post].join('|')}|$county|$state|$zipcode"
	    }
	}
	all.each{
		ps.println it
	}
	
	
}