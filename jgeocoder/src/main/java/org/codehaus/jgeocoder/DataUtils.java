package org.codehaus.jgeocoder;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
public class DataUtils{
    	
	/**
     * @return A pipe separated string of state abbrv, intends to be used in a regular expression
     */
    public static String getStateAbbrvRegex(){
        return "RI|VT|HI|ME|VA|MI|DE|ID|IA|MD|MA|AR|IL|UT|IN|MN|AZ|MO|MT|MS|NH|NJ|NM|AK|AL|TX|NC|ND|NE|NY|GA|NV|TN|CA|OK|OH|WY|FL|SD|SC|CT|WV|DC|WI|KY|KS|OR|LA|WA|CO|PA|" +
        		"R I|V T|H I|M E|V A|M I|D E|I D|I A|M D|M A|A R|I L|U T|I N|M N|A Z|M O|M T|M S|N H|N J|N M|A K|A L|T X|N C|N D|N E|N Y|G A|N V|T N|C A|O K|O H|W Y|F L|S D|S C|C T|W V|D C|W I|K Y|K S|O R|L A|W A|C O|P A";
    }
    
    
    /**
     * input s must be in upper case
     * 
     * @return the proper state abbrv if the input is one of the recognized state short names such as 
     * 'tenn', 'penn', etc. The input string otherwise. 
     */
    public static String translateStateShortName(String s){
        return ObjectUtils.nvl(STATENAME.get(s), s);
    }
    
    public static String expandLocalityAbbrv(String s, boolean isStreet){
    	if(s == null){
    		return null;
    	}
        String[] toks = s.split("[\\W&&[^']]+");
        for(int i =0; i<toks.length ; i++){
        	if(!isStreet && toks[i].equals("ST")){
        		toks[i] = "STREET";        		
        	}else{
        		toks[i] = ObjectUtils.nvl(LOCALITY_ABBRV_MAP.get(toks[i]), toks[i]);
        	}
        }
        return StringUtils.join(toks, ' ');
    }
    
    
    public static String expandState(String stateAbbrv){
    	return STATE_MAP.get(stateAbbrv);
    }
    
    /**
     * Remove stop words such as north, south, area, twp, township, midtown, central, upper, lower, metro, etc from input 
     * @param s
     * @return
     */
    public static String removeStopWords(String s){
        Matcher m = LOCALITY_STOP_WORDS.matcher(s);
        return m.replaceAll(" ").trim();
    }
    
    private static final Pattern LOCALITY_STOP_WORDS = Pattern.compile("\\s*\\b(north|south|east|west|northest|northwest|southeast|southwest|southern|northern|western|eastern|" +
    		"twp|township|midtown|borough|metro|upper|lower|central|mid|county)\\b\\s*");
    
    private static Map<String, String> LOCALITY_ABBRV_MAP = new HashMap<String, String>();
    private static Map<String, String> STATE_MAP = new HashMap<String, String>();
    
    private static Map<String, String> STATENAME = new HashMap<String, String>();
    
    static{        
    	LOCALITY_ABBRV_MAP.put("ALLEE", "ALLEY");
    	LOCALITY_ABBRV_MAP.put("ALLY", "ALLEY");
    	LOCALITY_ABBRV_MAP.put("ALY", "ALLEY");
    	LOCALITY_ABBRV_MAP.put("ANEX", "ANNEX");
    	LOCALITY_ABBRV_MAP.put("ANNX", "ANNEX");
    	LOCALITY_ABBRV_MAP.put("ANX", "ANNEX");
    	LOCALITY_ABBRV_MAP.put("ARC", "ARCADE");
    	LOCALITY_ABBRV_MAP.put("AV", "AVENUE");
    	LOCALITY_ABBRV_MAP.put("AVE", "AVENUE");
    	LOCALITY_ABBRV_MAP.put("AVEN", "AVENUE");
    	LOCALITY_ABBRV_MAP.put("AVENU", "AVENUE");
    	LOCALITY_ABBRV_MAP.put("AVN", "AVENUE");
    	LOCALITY_ABBRV_MAP.put("AVNUE", "AVENUE");
    	LOCALITY_ABBRV_MAP.put("BAYOU", "BAYOO");
    	LOCALITY_ABBRV_MAP.put("BCH", "BEACH");
    	LOCALITY_ABBRV_MAP.put("BEND", "BND");
    	LOCALITY_ABBRV_MAP.put("BG", "BURG");
    	LOCALITY_ABBRV_MAP.put("BGS", "BURGS");
    	LOCALITY_ABBRV_MAP.put("BLF", "BLUFF");
    	LOCALITY_ABBRV_MAP.put("BLFS", "BLUFFS");
    	LOCALITY_ABBRV_MAP.put("BLUF", "BLUFF");
    	LOCALITY_ABBRV_MAP.put("BLVD", "BOULEVARD");
    	LOCALITY_ABBRV_MAP.put("BND", "BEND");
    	LOCALITY_ABBRV_MAP.put("BOT", "BOTTOM");
    	LOCALITY_ABBRV_MAP.put("BOTTM", "BOTTOM");
    	LOCALITY_ABBRV_MAP.put("BOUL", "BOULEVARD");
    	LOCALITY_ABBRV_MAP.put("BOULV", "BOULEVARD");
    	LOCALITY_ABBRV_MAP.put("BR", "BRANCH");
    	LOCALITY_ABBRV_MAP.put("BRDGE", "BRIDGE");
    	LOCALITY_ABBRV_MAP.put("BRG", "BRIDGE");
    	LOCALITY_ABBRV_MAP.put("BRK", "BROOK");
    	LOCALITY_ABBRV_MAP.put("BRKS", "BROOKS");
    	LOCALITY_ABBRV_MAP.put("BRNCH", "BRANCH");
    	LOCALITY_ABBRV_MAP.put("BTM", "BOTTOM");
    	LOCALITY_ABBRV_MAP.put("BYP", "BYPASS");
    	LOCALITY_ABBRV_MAP.put("BYPA", "BYPASS");
    	LOCALITY_ABBRV_MAP.put("BYPAS", "BYPASS");
    	LOCALITY_ABBRV_MAP.put("BYPS", "BYPASS");
    	LOCALITY_ABBRV_MAP.put("BYU", "BAYOO");
    	LOCALITY_ABBRV_MAP.put("CANYN", "CANYON");
    	LOCALITY_ABBRV_MAP.put("CAUSWAY", "CAUSEWAY");
    	LOCALITY_ABBRV_MAP.put("CEN", "CENTER");
    	LOCALITY_ABBRV_MAP.put("CENT", "CENTER");
    	LOCALITY_ABBRV_MAP.put("CENTR", "CENTER");
    	LOCALITY_ABBRV_MAP.put("CENTRE", "CENTER");
    	LOCALITY_ABBRV_MAP.put("CIR", "CIRCLE");
    	LOCALITY_ABBRV_MAP.put("CIRC", "CIRCLE");
    	LOCALITY_ABBRV_MAP.put("CIRCL", "CIRCLE");
    	LOCALITY_ABBRV_MAP.put("CIRS", "CIRCLES");
    	LOCALITY_ABBRV_MAP.put("CK", "CREEK");
    	LOCALITY_ABBRV_MAP.put("CLB", "CLUB");
    	LOCALITY_ABBRV_MAP.put("CLF", "CLIFF");
    	LOCALITY_ABBRV_MAP.put("CLFS", "CLIFFS");
    	LOCALITY_ABBRV_MAP.put("CLG", "COLLEGE");
    	LOCALITY_ABBRV_MAP.put("CMN", "COMMON");
    	LOCALITY_ABBRV_MAP.put("CMP", "CAMP");
    	LOCALITY_ABBRV_MAP.put("CNTER", "CENTER");
    	LOCALITY_ABBRV_MAP.put("CNTR", "CENTER");
    	LOCALITY_ABBRV_MAP.put("CNYN", "CANYON");
    	LOCALITY_ABBRV_MAP.put("CO", "COUNTY");
    	LOCALITY_ABBRV_MAP.put("COR", "CORNER");
    	LOCALITY_ABBRV_MAP.put("CORS", "CORNERS");
    	LOCALITY_ABBRV_MAP.put("CP", "CAMP");
    	LOCALITY_ABBRV_MAP.put("CPE", "CAPE");
    	LOCALITY_ABBRV_MAP.put("CR", "CREEK");
    	LOCALITY_ABBRV_MAP.put("CRCL", "CIRCLE");
    	LOCALITY_ABBRV_MAP.put("CRCLE", "CIRCLE");
    	LOCALITY_ABBRV_MAP.put("CRECENT", "CRESCENT");
    	LOCALITY_ABBRV_MAP.put("CRES", "CRESCENT");
    	LOCALITY_ABBRV_MAP.put("CRESENT", "CRESCENT");
    	LOCALITY_ABBRV_MAP.put("CRK", "CREEK");
    	LOCALITY_ABBRV_MAP.put("CRSCNT", "CRESCENT");
    	LOCALITY_ABBRV_MAP.put("CRSE", "COURSE");
    	LOCALITY_ABBRV_MAP.put("CRSENT", "CRESCENT");
    	LOCALITY_ABBRV_MAP.put("CRSNT", "CRESCENT");
    	LOCALITY_ABBRV_MAP.put("CRSSING", "CROSSING");
    	LOCALITY_ABBRV_MAP.put("CRSSNG", "CROSSING");
    	LOCALITY_ABBRV_MAP.put("CRST", "CREST");
    	LOCALITY_ABBRV_MAP.put("CRT", "COURT");
    	LOCALITY_ABBRV_MAP.put("CSWY", "CAUSEWAY");
    	LOCALITY_ABBRV_MAP.put("CT", "COURTS");
    	LOCALITY_ABBRV_MAP.put("CTR", "CENTER");
    	LOCALITY_ABBRV_MAP.put("CTRS", "CENTERS");
    	LOCALITY_ABBRV_MAP.put("CTS", "COURTS");
    	LOCALITY_ABBRV_MAP.put("CTY", "CITY");
    	LOCALITY_ABBRV_MAP.put("CURV", "CURVE");
    	LOCALITY_ABBRV_MAP.put("CV", "COVE");
    	LOCALITY_ABBRV_MAP.put("CVS", "COVES");
    	LOCALITY_ABBRV_MAP.put("CY", "CITY");
    	LOCALITY_ABBRV_MAP.put("CYN", "CANYON");
    	LOCALITY_ABBRV_MAP.put("DIV", "DIVIDE");
    	LOCALITY_ABBRV_MAP.put("DL", "DALE");
    	LOCALITY_ABBRV_MAP.put("DM", "DAM");
    	LOCALITY_ABBRV_MAP.put("DR", "DRIVE");
    	LOCALITY_ABBRV_MAP.put("DRIV", "DRIVE");
    	LOCALITY_ABBRV_MAP.put("DRS", "DRIVES");
    	LOCALITY_ABBRV_MAP.put("DRV", "DRIVE");
    	LOCALITY_ABBRV_MAP.put("DV", "DIVIDE");
    	LOCALITY_ABBRV_MAP.put("DVD", "DIVIDE");
    	LOCALITY_ABBRV_MAP.put("E", "EAST");
    	LOCALITY_ABBRV_MAP.put("EST", "ESTATE");
    	LOCALITY_ABBRV_MAP.put("ESTS", "ESTATES");
    	LOCALITY_ABBRV_MAP.put("EXP", "EXPRESSWAY");
    	LOCALITY_ABBRV_MAP.put("EXPR", "EXPRESSWAY");
    	LOCALITY_ABBRV_MAP.put("EXPRESS", "EXPRESSWAY");
    	LOCALITY_ABBRV_MAP.put("EXPW", "EXPRESSWAY");
    	LOCALITY_ABBRV_MAP.put("EXPY", "EXPRESSWAY");
    	LOCALITY_ABBRV_MAP.put("EXT", "EXTENSION");
    	LOCALITY_ABBRV_MAP.put("EXTN", "EXTENSION");
    	LOCALITY_ABBRV_MAP.put("EXTNSN", "EXTENSION");
    	LOCALITY_ABBRV_MAP.put("EXTS", "EXTENSIONS");
    	LOCALITY_ABBRV_MAP.put("FLD", "FIELD");
    	LOCALITY_ABBRV_MAP.put("FLDS", "FIELDS");
    	LOCALITY_ABBRV_MAP.put("FLS", "FALLS");
    	LOCALITY_ABBRV_MAP.put("FLT", "FLAT");
    	LOCALITY_ABBRV_MAP.put("FLTS", "FLATS");
    	LOCALITY_ABBRV_MAP.put("FORESTS", "FOREST");
    	LOCALITY_ABBRV_MAP.put("FORG", "FORGE");
    	LOCALITY_ABBRV_MAP.put("FRD", "FORD");
    	LOCALITY_ABBRV_MAP.put("FRDS", "FORDS");
    	LOCALITY_ABBRV_MAP.put("FREEWY", "FREEWAY");
    	LOCALITY_ABBRV_MAP.put("FRG", "FORGE");
    	LOCALITY_ABBRV_MAP.put("FRGS", "FORGES");
    	LOCALITY_ABBRV_MAP.put("FRK", "FORK");
    	LOCALITY_ABBRV_MAP.put("FRKS", "FORKS");
    	LOCALITY_ABBRV_MAP.put("FRRY", "FERRY");
    	LOCALITY_ABBRV_MAP.put("FRST", "FOREST");
    	LOCALITY_ABBRV_MAP.put("FRT", "FORT");
    	LOCALITY_ABBRV_MAP.put("FRWAY", "FREEWAY");
    	LOCALITY_ABBRV_MAP.put("FRWY", "FREEWAY");
    	LOCALITY_ABBRV_MAP.put("FRY", "FERRY");
    	LOCALITY_ABBRV_MAP.put("FT", "FORT");
    	LOCALITY_ABBRV_MAP.put("FWY", "FREEWAY");
    	LOCALITY_ABBRV_MAP.put("GARDN", "GARDEN");
    	LOCALITY_ABBRV_MAP.put("GATEWY", "GATEWAY");
    	LOCALITY_ABBRV_MAP.put("GATWAY", "GATEWAY");
    	LOCALITY_ABBRV_MAP.put("GDN", "GARDEN");
    	LOCALITY_ABBRV_MAP.put("GDNS", "GARDENS");
    	LOCALITY_ABBRV_MAP.put("GLN", "GLEN");
    	LOCALITY_ABBRV_MAP.put("GLNS", "GLENS");
    	LOCALITY_ABBRV_MAP.put("GRDEN", "GARDEN");
    	LOCALITY_ABBRV_MAP.put("GRDN", "GARDEN");
    	LOCALITY_ABBRV_MAP.put("GRDNS", "GARDENS");
    	LOCALITY_ABBRV_MAP.put("GRN", "GREEN");
    	LOCALITY_ABBRV_MAP.put("GRNS", "GREENS");
    	LOCALITY_ABBRV_MAP.put("GROV", "GROVE");
    	LOCALITY_ABBRV_MAP.put("GRV", "GROVE");
    	LOCALITY_ABBRV_MAP.put("GRVS", "GROVES");
    	LOCALITY_ABBRV_MAP.put("GTWAY", "GATEWAY");
    	LOCALITY_ABBRV_MAP.put("GTWY", "GATEWY");
    	LOCALITY_ABBRV_MAP.put("HARB", "HARBOR");
    	LOCALITY_ABBRV_MAP.put("HARBR", "HARBOR");
    	LOCALITY_ABBRV_MAP.put("HAVN", "HAVEN");
    	LOCALITY_ABBRV_MAP.put("HBR", "HARBOR");
    	LOCALITY_ABBRV_MAP.put("HBRS", "HARBORS");
    	LOCALITY_ABBRV_MAP.put("HEIGHT", "HEIGHTS");
    	LOCALITY_ABBRV_MAP.put("HGT", "HEIGHT");
    	LOCALITY_ABBRV_MAP.put("HGTS", "HEIGHTS");
    	LOCALITY_ABBRV_MAP.put("HIGHWY", "HIGHWAY");
    	LOCALITY_ABBRV_MAP.put("HIWAY", "HIGHWAY");
    	LOCALITY_ABBRV_MAP.put("HIWY", "HIGHWAY");
    	LOCALITY_ABBRV_MAP.put("HL", "HILL");
    	LOCALITY_ABBRV_MAP.put("HLLW", "HOLLOW");
    	LOCALITY_ABBRV_MAP.put("HLS", "HILLS");
    	LOCALITY_ABBRV_MAP.put("HOLLOWS", "HOLLOW");
    	LOCALITY_ABBRV_MAP.put("HOLW", "HOLLOW");
    	LOCALITY_ABBRV_MAP.put("HOLWS", "HOLLOW");
    	LOCALITY_ABBRV_MAP.put("HRBOR", "HARBOR");
    	LOCALITY_ABBRV_MAP.put("HT", "HEIGHT");
    	LOCALITY_ABBRV_MAP.put("HTS", "HEIGHTS");
    	LOCALITY_ABBRV_MAP.put("HVN", "HAVEN");
    	LOCALITY_ABBRV_MAP.put("HWAY", "HIGHWAY");
    	LOCALITY_ABBRV_MAP.put("HWY", "HIGHWAY");
    	LOCALITY_ABBRV_MAP.put("INLT", "INLET");
    	LOCALITY_ABBRV_MAP.put("IS", "ISLAND");
    	LOCALITY_ABBRV_MAP.put("ISL", "ISLAND");
    	LOCALITY_ABBRV_MAP.put("ISLES", "ISLE");
    	LOCALITY_ABBRV_MAP.put("ISLND", "ISLAND");
    	LOCALITY_ABBRV_MAP.put("ISLNDS", "ISLANDS");
    	LOCALITY_ABBRV_MAP.put("ISS", "ISLANDS");
    	LOCALITY_ABBRV_MAP.put("JCT", "JUNCTION");
    	LOCALITY_ABBRV_MAP.put("JCTION", "JUNCTION");
    	LOCALITY_ABBRV_MAP.put("JCTN", "JUNCTION");
    	LOCALITY_ABBRV_MAP.put("JCTNS", "JUNCTIONS");
    	LOCALITY_ABBRV_MAP.put("JCTS", "JUNCTIONS");
    	LOCALITY_ABBRV_MAP.put("JUNCTN", "JUNCTION");
    	LOCALITY_ABBRV_MAP.put("JUNCTON", "JUNCTION");
    	LOCALITY_ABBRV_MAP.put("KNL", "KNOLL");
    	LOCALITY_ABBRV_MAP.put("KNLS", "KNOLLS");
    	LOCALITY_ABBRV_MAP.put("KNOL", "KNOLL");
    	LOCALITY_ABBRV_MAP.put("KY", "KEY");
    	LOCALITY_ABBRV_MAP.put("KYS", "KEYS");
    	LOCALITY_ABBRV_MAP.put("LA", "LANE");
    	LOCALITY_ABBRV_MAP.put("LANES", "LANE");
    	LOCALITY_ABBRV_MAP.put("LCK", "LOCK");
    	LOCALITY_ABBRV_MAP.put("LCKS", "LOCKS");
    	LOCALITY_ABBRV_MAP.put("LDG", "LODGE");
    	LOCALITY_ABBRV_MAP.put("LDGE", "LODGE");
    	LOCALITY_ABBRV_MAP.put("LF", "LOAF");
    	LOCALITY_ABBRV_MAP.put("LGT", "LIGHT");
    	LOCALITY_ABBRV_MAP.put("LGTS", "LIGHTS");
    	LOCALITY_ABBRV_MAP.put("LK", "LAKE");
    	LOCALITY_ABBRV_MAP.put("LKS", "LAKES");
    	LOCALITY_ABBRV_MAP.put("LN", "LANE");
    	LOCALITY_ABBRV_MAP.put("LNDG", "LANDING");
    	LOCALITY_ABBRV_MAP.put("LNDNG", "LANDING");
    	LOCALITY_ABBRV_MAP.put("LODG", "LODGE");
    	LOCALITY_ABBRV_MAP.put("LOOPS", "LOOP");
    	LOCALITY_ABBRV_MAP.put("MDW", "MEADOW");
    	LOCALITY_ABBRV_MAP.put("MDWS", "MEADOWS");
    	LOCALITY_ABBRV_MAP.put("MEDOWS", "MEADOWS");
    	LOCALITY_ABBRV_MAP.put("MISSN", "MISSION");
    	LOCALITY_ABBRV_MAP.put("ML", "MILL");
    	LOCALITY_ABBRV_MAP.put("MLS", "MILLS");
    	LOCALITY_ABBRV_MAP.put("MNR", "MANOR");
    	LOCALITY_ABBRV_MAP.put("MNRS", "MANORS");
    	LOCALITY_ABBRV_MAP.put("MNT", "MOUNT");
    	LOCALITY_ABBRV_MAP.put("MNTAIN", "MOUNTAIN");
    	LOCALITY_ABBRV_MAP.put("MNTN", "MOUNTAIN");
    	LOCALITY_ABBRV_MAP.put("MNTNS", "MOUNTAINS");
    	LOCALITY_ABBRV_MAP.put("MOUNTIN", "MOUNTAIN");
    	LOCALITY_ABBRV_MAP.put("MSN", "MISSION");
    	LOCALITY_ABBRV_MAP.put("MSSN", "MISSION");
    	LOCALITY_ABBRV_MAP.put("MT", "MOUNT");
    	LOCALITY_ABBRV_MAP.put("MTIN", "MOUNTAIN");
    	LOCALITY_ABBRV_MAP.put("MTN", "MOUNTAIN");
    	LOCALITY_ABBRV_MAP.put("MTNS", "MOUNTAINS");
    	LOCALITY_ABBRV_MAP.put("MTWY", "MOTORWAY");
    	LOCALITY_ABBRV_MAP.put("N", "NORTH");
    	LOCALITY_ABBRV_MAP.put("NCK", "NECK");
    	LOCALITY_ABBRV_MAP.put("NE", "NORTHEAST");
    	LOCALITY_ABBRV_MAP.put("NW", "NORTHWEST");
    	LOCALITY_ABBRV_MAP.put("OPAS", "OVERPASS");
    	LOCALITY_ABBRV_MAP.put("ORCH", "ORCHARD");
    	LOCALITY_ABBRV_MAP.put("ORCHRD", "ORCHARD");
    	LOCALITY_ABBRV_MAP.put("OVL", "OVAL");
    	LOCALITY_ABBRV_MAP.put("OZ", "OZONE");
    	LOCALITY_ABBRV_MAP.put("PARK", "PARKS");
    	LOCALITY_ABBRV_MAP.put("PARKWY", "PARKWAY");
    	LOCALITY_ABBRV_MAP.put("PATHS", "PATH");
    	LOCALITY_ABBRV_MAP.put("PIKES", "PIKE");
    	LOCALITY_ABBRV_MAP.put("PK", "PARK");
    	LOCALITY_ABBRV_MAP.put("PKWAY", "PARKWAY");
    	LOCALITY_ABBRV_MAP.put("PKWY", "PARKWAYS");
    	LOCALITY_ABBRV_MAP.put("PKWYS", "PARKWAYS");
    	LOCALITY_ABBRV_MAP.put("PKY", "PARKWAY");
    	LOCALITY_ABBRV_MAP.put("PL", "PLACE");
    	LOCALITY_ABBRV_MAP.put("PLAINES", "PLAINS");
    	LOCALITY_ABBRV_MAP.put("PLN", "PLAIN");
    	LOCALITY_ABBRV_MAP.put("PLNS", "PLAINS");
    	LOCALITY_ABBRV_MAP.put("PLZ", "PLAZA");
    	LOCALITY_ABBRV_MAP.put("PLZA", "PLAZA");
    	LOCALITY_ABBRV_MAP.put("PNE", "PINE");
    	LOCALITY_ABBRV_MAP.put("PNES", "PINES");
    	LOCALITY_ABBRV_MAP.put("PR", "PRAIRIE");
    	LOCALITY_ABBRV_MAP.put("PRARIE", "PRAIRIE");
    	LOCALITY_ABBRV_MAP.put("PRK", "PARK");
    	LOCALITY_ABBRV_MAP.put("PRR", "PRAIRIE");
    	LOCALITY_ABBRV_MAP.put("PRT", "PORT");
    	LOCALITY_ABBRV_MAP.put("PRTS", "PORTS");
    	LOCALITY_ABBRV_MAP.put("PSGE", "PASSAGE");
    	LOCALITY_ABBRV_MAP.put("PT", "POINT");
    	LOCALITY_ABBRV_MAP.put("PTS", "POINTS");
    	LOCALITY_ABBRV_MAP.put("RAD", "RADIAL");
    	LOCALITY_ABBRV_MAP.put("RADIEL", "RADIAL");
    	LOCALITY_ABBRV_MAP.put("RADL", "RADIAL");
    	LOCALITY_ABBRV_MAP.put("RANCHES", "RANCH");
    	LOCALITY_ABBRV_MAP.put("RD", "ROAD");
    	LOCALITY_ABBRV_MAP.put("RDG", "RIDGE");
    	LOCALITY_ABBRV_MAP.put("RDGE", "RIDGE");
    	LOCALITY_ABBRV_MAP.put("RDGS", "RIDGES");
    	LOCALITY_ABBRV_MAP.put("RDS", "ROADS");
    	LOCALITY_ABBRV_MAP.put("RIV", "RIVER");
    	LOCALITY_ABBRV_MAP.put("RIVR", "RIVER");
    	LOCALITY_ABBRV_MAP.put("RNCH", "RANCH");
    	LOCALITY_ABBRV_MAP.put("RNCHS", "RANCH");
    	LOCALITY_ABBRV_MAP.put("RPD", "RAPID");
    	LOCALITY_ABBRV_MAP.put("RPDS", "RAPIDS");
    	LOCALITY_ABBRV_MAP.put("RST", "REST");
    	LOCALITY_ABBRV_MAP.put("RTE", "ROUTE");
    	LOCALITY_ABBRV_MAP.put("RVR", "RIVER");
    	LOCALITY_ABBRV_MAP.put("S", "SOUTH");
    	LOCALITY_ABBRV_MAP.put("SE", "SOUTHEAST");
    	LOCALITY_ABBRV_MAP.put("SHL", "SHOAL");
    	LOCALITY_ABBRV_MAP.put("SHLS", "SHOALS");
    	LOCALITY_ABBRV_MAP.put("SHOAR", "SHORE");
    	LOCALITY_ABBRV_MAP.put("SHOARS", "SHORES");
    	LOCALITY_ABBRV_MAP.put("SHR", "SHORE");
    	LOCALITY_ABBRV_MAP.put("SHRS", "SHORES");
    	LOCALITY_ABBRV_MAP.put("SKWY", "SKYWAY");
    	LOCALITY_ABBRV_MAP.put("SMT", "SUMMIT");
    	LOCALITY_ABBRV_MAP.put("SPG", "SPRING");
    	LOCALITY_ABBRV_MAP.put("SPGS", "SPRINGS");
    	LOCALITY_ABBRV_MAP.put("SPNG", "SPRING");
    	LOCALITY_ABBRV_MAP.put("SPNGS", "SPRINGS");
    	LOCALITY_ABBRV_MAP.put("SPRNG", "SPRING");
    	LOCALITY_ABBRV_MAP.put("SPRNGS", "SPRINGS");
    	LOCALITY_ABBRV_MAP.put("SPUR", "SPURS");
    	LOCALITY_ABBRV_MAP.put("SQ", "SQUARE");
    	LOCALITY_ABBRV_MAP.put("SQR", "SQUARE");
    	LOCALITY_ABBRV_MAP.put("SQRE", "SQUARE");
    	LOCALITY_ABBRV_MAP.put("SQRS", "SQUARES");
    	LOCALITY_ABBRV_MAP.put("SQS", "SQUARES");
    	LOCALITY_ABBRV_MAP.put("SQU", "SQUARE");
    	LOCALITY_ABBRV_MAP.put("ST", "SAINT");
    	LOCALITY_ABBRV_MAP.put("STA", "STATION");
    	LOCALITY_ABBRV_MAP.put("STATN", "STATION");
    	LOCALITY_ABBRV_MAP.put("STN", "STATION");
    	LOCALITY_ABBRV_MAP.put("STR", "STREET");
    	LOCALITY_ABBRV_MAP.put("STRA", "STRAVENUE");
    	LOCALITY_ABBRV_MAP.put("STRAV", "STRAVENUE");
    	LOCALITY_ABBRV_MAP.put("STRAVE", "STRAVENUE");
    	LOCALITY_ABBRV_MAP.put("STRAVEN", "STRAVENUE");
    	LOCALITY_ABBRV_MAP.put("STRAVN", "STRAVENUE");
    	LOCALITY_ABBRV_MAP.put("STREME", "STREAM");
    	LOCALITY_ABBRV_MAP.put("STRM", "STREAM");
    	LOCALITY_ABBRV_MAP.put("STRT", "STREET");
    	LOCALITY_ABBRV_MAP.put("STRVN", "STRAVENUE");
    	LOCALITY_ABBRV_MAP.put("STRVNUE", "STRAVENUE");
    	LOCALITY_ABBRV_MAP.put("STS", "STREETS");
    	LOCALITY_ABBRV_MAP.put("SUMIT", "SUMMIT");
    	LOCALITY_ABBRV_MAP.put("SUMITT", "SUMMIT");
    	LOCALITY_ABBRV_MAP.put("SW", "SOUTHWEST");
    	LOCALITY_ABBRV_MAP.put("TER", "TERRACE");
    	LOCALITY_ABBRV_MAP.put("TERR", "TERRACE");
    	LOCALITY_ABBRV_MAP.put("TPK", "TURNPIKE");
    	LOCALITY_ABBRV_MAP.put("TPKE", "TURNPIKE");
    	LOCALITY_ABBRV_MAP.put("TR", "TRAIL");
    	LOCALITY_ABBRV_MAP.put("TRACES", "TRACE");
    	LOCALITY_ABBRV_MAP.put("TRACKS", "TRACK");
    	LOCALITY_ABBRV_MAP.put("TRAILER", "TRLR");
    	LOCALITY_ABBRV_MAP.put("TRAILS", "TRAIL");
    	LOCALITY_ABBRV_MAP.put("TRAK", "TRACK");
    	LOCALITY_ABBRV_MAP.put("TRCE", "TRACE");
    	LOCALITY_ABBRV_MAP.put("TRFY", "TRAFFICWAY");
    	LOCALITY_ABBRV_MAP.put("TRK", "TRACK");
    	LOCALITY_ABBRV_MAP.put("TRKS", "TRACK");
    	LOCALITY_ABBRV_MAP.put("TRL", "TRAIL");
    	LOCALITY_ABBRV_MAP.put("TRLS", "TRAIL");
    	LOCALITY_ABBRV_MAP.put("TRNPK", "TURNPIKE");
    	LOCALITY_ABBRV_MAP.put("TRPK", "TURNPIKE");
    	LOCALITY_ABBRV_MAP.put("TRWY", "THROUGHWAY");
    	LOCALITY_ABBRV_MAP.put("TUNEL", "TUNNEL");
    	LOCALITY_ABBRV_MAP.put("TUNL", "TUNEL");
    	LOCALITY_ABBRV_MAP.put("TUNLS", "TUNNEL");
    	LOCALITY_ABBRV_MAP.put("TUNNELS", "TUNNEL");
    	LOCALITY_ABBRV_MAP.put("TUNNL", "TUNNEL");
    	LOCALITY_ABBRV_MAP.put("TURNPK", "TURNPIKE");
    	LOCALITY_ABBRV_MAP.put("UN", "UNION");
    	LOCALITY_ABBRV_MAP.put("UNS", "UNIONS");
    	LOCALITY_ABBRV_MAP.put("UPAS", "UNDERPASS");
    	LOCALITY_ABBRV_MAP.put("VAL", "VALLEY");
    	LOCALITY_ABBRV_MAP.put("VALLY", "VALLEY");
    	LOCALITY_ABBRV_MAP.put("VDCT", "VIADUCT");
    	LOCALITY_ABBRV_MAP.put("VIA", "VIADUCT");
    	LOCALITY_ABBRV_MAP.put("VIADCT", "VIADUCT");
    	LOCALITY_ABBRV_MAP.put("VIL", "VILLLAGE");
    	LOCALITY_ABBRV_MAP.put("VILL", "VILLLAGE");
    	LOCALITY_ABBRV_MAP.put("VILLAG", "VILLAGE");
    	LOCALITY_ABBRV_MAP.put("VILLG", "VILLAGE");
    	LOCALITY_ABBRV_MAP.put("VILLIAGE", "VILLAGE");
    	LOCALITY_ABBRV_MAP.put("VIS", "VISTA");
    	LOCALITY_ABBRV_MAP.put("VIST", "VISTA");
    	LOCALITY_ABBRV_MAP.put("VL", "VILLE");
    	LOCALITY_ABBRV_MAP.put("VLG", "VILLIAGE");
    	LOCALITY_ABBRV_MAP.put("VLGS", "VILLAGES");
    	LOCALITY_ABBRV_MAP.put("VLLY", "VALLEY");
    	LOCALITY_ABBRV_MAP.put("VLLYS", "VALLEYS");
    	LOCALITY_ABBRV_MAP.put("VLY", "VALLEY");
    	LOCALITY_ABBRV_MAP.put("VLYS", "VALLEYS");
    	LOCALITY_ABBRV_MAP.put("VST", "VISTA");
    	LOCALITY_ABBRV_MAP.put("VSTA", "VISTA");
    	LOCALITY_ABBRV_MAP.put("VW", "VIEW");
    	LOCALITY_ABBRV_MAP.put("VWS", "VIEWS");
    	LOCALITY_ABBRV_MAP.put("W", "WEST");
    	LOCALITY_ABBRV_MAP.put("WALK", "WALKS");
    	LOCALITY_ABBRV_MAP.put("WL", "WELL");
    	LOCALITY_ABBRV_MAP.put("WLS", "WELLS");
    	LOCALITY_ABBRV_MAP.put("WY", "WAY");
    	LOCALITY_ABBRV_MAP.put("XING", "CROSSING");
    	LOCALITY_ABBRV_MAP.put("XRD", "CROSSROAD");
        
    	STATE_MAP.put("AS", "AMERICAN SAMOA");
    	STATE_MAP.put("WV", "WEST VIRGINIA");
    	STATE_MAP.put("OK", "OKLAHOMA");
    	STATE_MAP.put("PR", "PUERTORICO");
    	STATE_MAP.put("WV", "WEST VIRGINIA");
    	STATE_MAP.put("MH", "MARSHALL ISLANDS");
    	STATE_MAP.put("IA", "IOWA");
    	STATE_MAP.put("NM", "NEW MEXICO");
    	STATE_MAP.put("NE", "NEBRASKA");
    	STATE_MAP.put("NH", "NEW HAMPSHIRE");
    	STATE_MAP.put("PA", "PENNSYLVANIA");
    	STATE_MAP.put("WA", "WASHINGTON");
    	STATE_MAP.put("SD", "SOUTH DAKOTA");
    	STATE_MAP.put("AZ", "ARIZONA");
    	STATE_MAP.put("NM", "NEWMEXICO");
    	STATE_MAP.put("AR", "ARKANSAS");
    	STATE_MAP.put("NC", "NORTH CAROLINA");
    	STATE_MAP.put("HI", "HAWAII");
    	STATE_MAP.put("DE", "DELAWARE");
    	STATE_MAP.put("NV", "NEVADA");
    	STATE_MAP.put("NJ", "NEWJERSEY");
    	STATE_MAP.put("NY", "NEW YORK");
    	STATE_MAP.put("RI", "RHODE ISLAND");
    	STATE_MAP.put("GA", "GEORGIA");
    	STATE_MAP.put("VI", "VIRGIN ISLANDS");
    	STATE_MAP.put("SC", "SOUTH CAROLINA");
    	STATE_MAP.put("LA", "LOUISIANA");
    	STATE_MAP.put("SD", "SOUTH DAKOTA");
    	STATE_MAP.put("ID", "IDAHO");
    	STATE_MAP.put("MO", "MISSOURI");
    	STATE_MAP.put("PR", "PUERTO RICO");
    	STATE_MAP.put("MA", "MASSACHUSETTS");
    	STATE_MAP.put("IN", "INDIANA");
    	STATE_MAP.put("CT", "CONNECTICUT");
    	STATE_MAP.put("CO", "COLORADO");
    	STATE_MAP.put("TX", "TEXAS");
    	STATE_MAP.put("VT", "VERMONT");
    	STATE_MAP.put("OH", "OHIO");
    	STATE_MAP.put("ND", "N DAKOTA");
    	STATE_MAP.put("VA", "VIRGINIA");
    	STATE_MAP.put("FL", "FLORIDA");
    	STATE_MAP.put("CA", "CALIFORNIA");
    	STATE_MAP.put("KS", "KANSAS");
    	STATE_MAP.put("MH", "MARSHALL IS");
    	STATE_MAP.put("AL", "ALABAMA");
    	STATE_MAP.put("PW", "PALAU");
    	STATE_MAP.put("IL", "ILLINOIS");
    	STATE_MAP.put("AK", "ALASKA");
    	STATE_MAP.put("MN", "MINNESOTA");
    	STATE_MAP.put("MT", "MONTANA");
    	STATE_MAP.put("SC", "SOUTH CAROLINA");
    	STATE_MAP.put("UT", "UTAH");
    	STATE_MAP.put("GU", "GUAM");
    	STATE_MAP.put("MD", "MARYLAND");
    	STATE_MAP.put("MI", "MICHIGAN");
    	STATE_MAP.put("KY", "KENTUCKY");
    	STATE_MAP.put("TN", "TENNESSEE");
    	STATE_MAP.put("WY", "WYOMING");
    	STATE_MAP.put("ND", "NORTH DAKOTA");
    	STATE_MAP.put("ME", "MAINE");
    	STATE_MAP.put("NY", "NEWYORK");
    	STATE_MAP.put("MP", "NORTHERN MARIANA ISLANDS");
    	STATE_MAP.put("WI", "WISCONSIN");
    	STATE_MAP.put("FM", "FEDERATED STATES OF MICRONESIA");
    	STATE_MAP.put("VI", "VIRGIN ISLAND");
    	STATE_MAP.put("DC", "DISTRICT OF COLUMBIA");
    	STATE_MAP.put("OR", "OREGON");
    	STATE_MAP.put("MS", "MISSISSIPPI");
    	STATE_MAP.put("NJ", "NEW JERSEY");

    	
        STATENAME.put("OKLA", "OK");
        STATENAME.put("NEBR", "NE");
        STATENAME.put("PENN", "PA");
        STATENAME.put("WASH", "WA");
        STATENAME.put("MISS", "MS");
        STATENAME.put("MASS", "MA");
        STATENAME.put("CONN", "CT");
        STATENAME.put("COLO", "CO");
        STATENAME.put("FLA", "FL");
        STATENAME.put("CALI", "CA");
        STATENAME.put("ARK", "AR");
        STATENAME.put("ALA", "AL");
        STATENAME.put("ILL", "IL");
        STATENAME.put("IND", "IN");
        STATENAME.put("MINN", "MN");
        STATENAME.put("MICH", "MI");
        STATENAME.put("TENN", "TN");
        
    }
}