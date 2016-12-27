part of swagger.api;


@Entity()
class SecuritySchemeDefinition {
  
  @Property(name: 'description')
  String description = null;
  

  @Property(name: 'type')
  String type = null;
  
  SecuritySchemeDefinition();

  @override
  String toString()  {
    return 'SecuritySchemeDefinition[description=$description, type=$type, ]';
  }

}

