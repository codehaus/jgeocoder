/**
 * 
 */
package org.codehaus.jgeocoder

import java.util.zip.ZipFile
import org.apache.commons.lang.StringUtils

def STATE = 'PA'def zip = new ZipFile('/home/liangj01/Desktop/TGR42101.ZIP')
def entries = zip.entries()

while(entries.hasMoreElements()){
	process(zip, entries.nextElement(), STATE)
	
}

void process(def zip, def entry, def state){
	if(!entry.getName().toUpperCase().endsWith('RT1')) return
	def ps = new PrintStream('street_dict.txt')
	def reader = new BufferedReader(new InputStreamReader(zip.getInputStream(entry)))
	def ext = entry.name.substring(entry.name.lastIndexOf('.')+1)
	if(ext == 'MET') return
	def tableName = 'tiger_'+ext.toLowerCase().substring(2)
	TigerTable table
	TigerDefinition.TIGER_TABLES.each{
	    if(it.name == tableName) table = it
	}

	reader.eachLine{line ->
	    if(StringUtils.isBlank(line)) return
	    def values = [:]
	    table.columns.each{ c ->
		    def val = line[c.range].trim()
		    val = StringUtils.upperCase(val) 
		    values[c.name] = val
	    }
	    
	    String street = values['FENAME']
	    def streetTokens = street.split(/\s+/)
	    def word, prev, post
	    for(int i =0; i< streetTokens.length; i++){
	    	
	    	word = streetTokens[i]
	    	prev = i>0? streetTokens[i-1] : ''
	        post = i<(streetTokens.length-1)? streetTokens[i+1] : values['FETYPE']
	        if(StringUtils.isBlank(word) || word.length() < 3) continue
	        ps.println "${[prev, word, post].join('|')}|$state"
	    }
	}
	
}