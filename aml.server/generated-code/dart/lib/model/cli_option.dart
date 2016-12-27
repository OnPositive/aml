part of swagger.api;


@Entity()
class CliOption {
  
  @Property(name: 'default')
  String _default = null;
  

  @Property(name: 'description')
  String description = null;
  

  @Property(name: 'enum')
  Map<String, String> _enum = {};
  

  @Property(name: 'optionName')
  String optionName = null;
  
/* Data type is based on the types supported by the JSON-Schema */
  @Property(name: 'type')
  String type = null;
  
  CliOption();

  @override
  String toString()  {
    return 'CliOption[_default=$_default, description=$description, _enum=$_enum, optionName=$optionName, type=$type, ]';
  }

}

