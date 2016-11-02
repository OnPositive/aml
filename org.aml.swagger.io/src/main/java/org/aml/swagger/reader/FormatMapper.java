package org.aml.swagger.reader;

class FormatMapper{
	public FormatMapper(String type2, String format2) {
		this.type=type2;
		this.format=format2;
	}
	String type;
	String format;
	
	public static FormatMapper mapFormat(FormatMapper mp){
		String format=mp.format;
		String type=mp.type;
		if (format != null) {
			if (format.equals("date-time")) {
				format = null;
				type = "date-time";
			} else if (format.equals("uuid")) {
				type="commons.uuid";
				format = null;
			} else if (format.equals("file")) {
				format = null;
			} else if (format.equals("password")) {
				type="commons.password";
				format = null;
			} else if (format.equals("string")) {
				format = null;
			} else if (format.equals("url")) {
				type="commons.url";
				format = null;
			} else if (format.equals("date-time-rfc1123")) {
				format = null;
			}
			else if (format.equals("enum")) {
				format = null;					
			}
			else if (format.equals("date-time")) {
				type = format;
				format = null;
			}
			else if (format.equals("date")) {
				type = "date-time";
				format = null;
			}
			else if (format.equals("dateTime")) {
				type = "date-time";
				format = null;
			}
			else if (format.equals("uuid")) {
				type="commons.uuid";
				format = null;
			} else if (format.equals("string")) {
				format = null;
			} else if (format.equals("password")) {
				type="commons.password";
				format = null;
			} else if (format.equals("non-iso-duration")) {
				format = null;
			} else if (format.equals("duration")) {
				format = null;
			} else if (format.equals("url")) {
				type="commons.url";
				format = null;
			} else if (format.equals("date-time-rfc1123")) {
				format = null;
			} else if (format.equals("unixtime")) {
				format = null;
			} else if (format.equals("base64url")) {
				type="commons.base64url";
				format = null;
			} else if (format.equals("byte")) {
				format = null;
			}
			else if (format != null && format.equals("file")) {
				format = null;
			}
			else if (format != null && format.equals("string")) {
				format = null;
			}
			else if (format != null && format.equals("uri")) {
				format = null;
			}
			else if (format != null && format.equals("base64url")) {
				type="commons.base64url";
				format = null;
			}
			else if (format != null && format.equals("unixtime")) {
				format = null;
			}
			else if (format != null && format.equals("date-time")) {
				format = null;
				type = "datetime";
			}
			else if (format != null && format.equals("date")) {
				format = null;

			}
			else if (format.equals("integer")){
				format=null;
				type="integer";
			}
			else if (format.equals("int64")){
				type="integer";
			}
			else if (format.equals("int32")){
				type="integer";
			}
			else if (format.equals("float")){
				type="number";
			}
			else if (format.equals("float32")){
				type="number";
				format="float";
			}
			else if (format.equals("double")){
				type="number";
			}
			else if (format.equals("cisco-sa-XXX")){
				format=null;
			}
			else if (format.equals("CVE-YYYY-NNNN")){
				format=null;
			}
			else if (format.equals("number")){
				format=null;
			}
			else if (format.equals("YYYY")){
				format=null;
			}
			else if (format.equals("email")){
				type="commons.email";
				format=null;
			}
			else if (format.equals("date-format")){
				format=null;
			}
			else if (format.equals("int8")){
				format="int8";
			}
			else if (format.equals("locale")){
				format=null;
			}
			else if (format.equals("id64")){
				format=null;
			}
			else if (format.equals("countrycode")){
				type="commons.countrycode";
				format=null;
			}
			else if (format.equals("timezone")){
				format=null;
			}
			else if (format.equals("extid")){
				format=null;
			}
			else if (format.equals("DateTime")){
				format=null;
			}
			else if (format.equals("ipv4")){
				type="commons.ipv4";
				format=null;
			}
			else if (format.equals("ipv6")){
				type="commons.ipv6";
				format=null;
			}
			else if (format.equals("binary")){
				format=null;
			}
			else if (format.equals("command separated list")){
				format=null;
			}
			else if (format.equals("json")){
				type="commons.json";
				format=null;
			}
			else if (format!=null){
				System.out.println(format);
			}
		}
		
		mp.format=format;
		mp.type=type;
		return mp;
	}
}