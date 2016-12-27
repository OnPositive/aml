part of swagger.api;


@Entity()
class ResponseCode {
  /* File download code */
  @Property(name: 'code')
  String code = null;
  
/* URL for fetching the generated client */
  @Property(name: 'link')
  String link = null;
  
  ResponseCode();

  @override
  String toString()  {
    return 'ResponseCode[code=$code, link=$link, ]';
  }

}

