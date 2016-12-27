part of swagger.api;


@Entity()
class GeneratorInput {
  
  @Property(name: 'authorizationValue')
  AuthorizationValue authorizationValue = null;
  

  @Property(name: 'options')
  Map<String, String> options = {};
  

  @Property(name: 'securityDefinition')
  SecuritySchemeDefinition securityDefinition = null;
  

  @Property(name: 'spec')
  Object spec = null;
  

  @Property(name: 'swaggerUrl')
  String swaggerUrl = null;
  
  GeneratorInput();

  @override
  String toString()  {
    return 'GeneratorInput[authorizationValue=$authorizationValue, options=$options, securityDefinition=$securityDefinition, spec=$spec, swaggerUrl=$swaggerUrl, ]';
  }

}

