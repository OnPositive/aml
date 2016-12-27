part of swagger.api;


@Entity()
class AuthorizationValue {
  
  @Property(name: 'keyName')
  String keyName = null;
  

  @Property(name: 'type')
  String type = null;
  

  @Property(name: 'value')
  String value = null;
  
  AuthorizationValue();

  @override
  String toString()  {
    return 'AuthorizationValue[keyName=$keyName, type=$type, value=$value, ]';
  }

}

