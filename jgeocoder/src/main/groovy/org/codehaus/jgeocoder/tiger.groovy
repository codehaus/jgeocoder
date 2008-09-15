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


def ps = new PrintStream(new FileOutputStream('tiger.txt', true))

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

	def code = filename.substring(3,8)
	reader.eachLine{line ->
	    if(StringUtils.isBlank(line)) return
	    def values = [:]
	    table.columns.each{ c ->
		    def val = line[c.range].trim()
		    val = StringUtils.upperCase(val) 
		    values[c.name] = val
	    }
	    
	    def tlid = values['TLID']
	    def fedirp = values['FEDIRP']
	    def fedirs = values['FEDIRS']
	    def fraddl = values['FRADDL']
	    def fraddr = values['FRADDR']
	    def toaddl = values['TOADDL']
	    def toaddr = values['TOADDR']
	    def frlong = values['FRLONG']
	    def tolong = values['TOLONG']
	    def frlat = values['FRLAT']
	    def tolat = values['TOLAT']
	    def fetype = values['FETYPE']
		def zipcode = values['ZIPL']
		if(StringUtils.isEmpty(zipcode)){
			zipcode = values['ZIPR']
	    }	    
	    String street = values['FENAME']
	    if(StringUtils.isNotEmpty(zipcode) && StringUtils.isNotEmpty(street)){
	    	ps.println "${[tlid, fraddl, fraddr, toaddl, toaddr, fedirp, street, fedirs, fetype, zipcode, frlat, tolat, frlong, tolong].join('|')}"
	    }
	    
	}
	
	
	
}