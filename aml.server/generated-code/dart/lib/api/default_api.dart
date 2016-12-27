part of swagger.api;



class DefaultApi {
  final ApiClient apiClient;

  DefaultApi([ApiClient apiClient]) : apiClient = apiClient ?? defaultApiClient;

  /// Gets languages supported by the client generator
  ///
  /// 
  Future<List<String>> genClientsGet( {  bool justIgnoreThisFlag: true}) async {
    if (!justIgnoreThisFlag) {
      print('Why???   Just trust me, I only need this variable inside the mustache codegen template.');
      // This code may be removed as soon as dart accepts trailing spaces (has already been implemented).
    }
    Object postBody = null;

    // verify required params are set

    // create path and map variables
    String path = "/gen/clients".replaceAll("{format}","json");

    // query params
    List<QueryParam> queryParams = [];
    Map<String, String> headerParams = {};
    Map<String, String> formParams = {};
    
    List<String> contentTypes = [];

    String contentType = contentTypes.length > 0 ? contentTypes[0] : "application/json";
    List<String> authNames = [];

    if(contentType.startsWith("multipart/form-data")) {
      bool hasFields = false;
      MultipartRequest mp = new MultipartRequest(null, null);
      
      if(hasFields)
        postBody = mp;
    }
    else {
          }

    var response = await apiClient.invokeAPI(path,
                                             'GET',
                                             queryParams,
                                             postBody,
                                             headerParams,
                                             formParams,
                                             contentType,
                                             authNames);

    if(response.statusCode >= 400) {
      throw new ApiException(response.statusCode, response.body);
    } else if(response.body != null) {
      return apiClient.deserialize(response.body, 'List<String>') as List<String> ;
    } else {
      return null;
    }
  }
  /// Returns options for a client library
  ///
  /// 
  Future<Map<String, CliOption>> genClientsLanguageGet(String language,  {  bool justIgnoreThisFlag: true}) async {
    if (!justIgnoreThisFlag) {
      print('Why???   Just trust me, I only need this variable inside the mustache codegen template.');
      // This code may be removed as soon as dart accepts trailing spaces (has already been implemented).
    }
    Object postBody = null;

    // verify required params are set
    if(language == null) {
     throw new ApiException(400, "Missing required param: language");
    }

    // create path and map variables
    String path = "/gen/clients/{language}".replaceAll("{format}","json").replaceAll("{" + "language" + "}", language.toString());

    // query params
    List<QueryParam> queryParams = [];
    Map<String, String> headerParams = {};
    Map<String, String> formParams = {};
    
    List<String> contentTypes = [];

    String contentType = contentTypes.length > 0 ? contentTypes[0] : "application/json";
    List<String> authNames = [];

    if(contentType.startsWith("multipart/form-data")) {
      bool hasFields = false;
      MultipartRequest mp = new MultipartRequest(null, null);
      
      if(hasFields)
        postBody = mp;
    }
    else {
          }

    var response = await apiClient.invokeAPI(path,
                                             'GET',
                                             queryParams,
                                             postBody,
                                             headerParams,
                                             formParams,
                                             contentType,
                                             authNames);

    if(response.statusCode >= 400) {
      throw new ApiException(response.statusCode, response.body);
    } else if(response.body != null) {
      return apiClient.deserialize(response.body, 'Map<String, CliOption>') as Map<String, CliOption> ;
    } else {
      return null;
    }
  }
  /// Generates a client library
  ///
  /// Accepts a &#x60;GeneratorInput&#x60; options map for spec location and generation options
  Future<ResponseCode> genClientsLanguagePost(String language,  {  bool justIgnoreThisFlag: true}) async {
    if (!justIgnoreThisFlag) {
      print('Why???   Just trust me, I only need this variable inside the mustache codegen template.');
      // This code may be removed as soon as dart accepts trailing spaces (has already been implemented).
    }
    Object postBody = null;

    // verify required params are set
    if(language == null) {
     throw new ApiException(400, "Missing required param: language");
    }

    // create path and map variables
    String path = "/gen/clients/{language}".replaceAll("{format}","json").replaceAll("{" + "language" + "}", language.toString());

    // query params
    List<QueryParam> queryParams = [];
    Map<String, String> headerParams = {};
    Map<String, String> formParams = {};
    
    List<String> contentTypes = [];

    String contentType = contentTypes.length > 0 ? contentTypes[0] : "application/json";
    List<String> authNames = [];

    if(contentType.startsWith("multipart/form-data")) {
      bool hasFields = false;
      MultipartRequest mp = new MultipartRequest(null, null);
      
      if(hasFields)
        postBody = mp;
    }
    else {
          }

    var response = await apiClient.invokeAPI(path,
                                             'POST',
                                             queryParams,
                                             postBody,
                                             headerParams,
                                             formParams,
                                             contentType,
                                             authNames);

    if(response.statusCode >= 400) {
      throw new ApiException(response.statusCode, response.body);
    } else if(response.body != null) {
      return apiClient.deserialize(response.body, 'ResponseCode') as ResponseCode ;
    } else {
      return null;
    }
  }
  /// Downloads a pre-generated file
  ///
  /// A valid &#x60;fileId&#x60; is generated by the &#x60;/clients/{language}&#x60; or &#x60;/servers/{language}&#x60; POST operations.  The fileId code can be used just once, after which a new &#x60;fileId&#x60; will need to be requested.
  Future<String> genDownloadFileIdGet(String fileId,  {  bool justIgnoreThisFlag: true}) async {
    if (!justIgnoreThisFlag) {
      print('Why???   Just trust me, I only need this variable inside the mustache codegen template.');
      // This code may be removed as soon as dart accepts trailing spaces (has already been implemented).
    }
    Object postBody = null;

    // verify required params are set
    if(fileId == null) {
     throw new ApiException(400, "Missing required param: fileId");
    }

    // create path and map variables
    String path = "/gen/download/{fileId}".replaceAll("{format}","json").replaceAll("{" + "fileId" + "}", fileId.toString());

    // query params
    List<QueryParam> queryParams = [];
    Map<String, String> headerParams = {};
    Map<String, String> formParams = {};
    
    List<String> contentTypes = [];

    String contentType = contentTypes.length > 0 ? contentTypes[0] : "application/json";
    List<String> authNames = [];

    if(contentType.startsWith("multipart/form-data")) {
      bool hasFields = false;
      MultipartRequest mp = new MultipartRequest(null, null);
      
      if(hasFields)
        postBody = mp;
    }
    else {
          }

    var response = await apiClient.invokeAPI(path,
                                             'GET',
                                             queryParams,
                                             postBody,
                                             headerParams,
                                             formParams,
                                             contentType,
                                             authNames);

    if(response.statusCode >= 400) {
      throw new ApiException(response.statusCode, response.body);
    } else if(response.body != null) {
      return apiClient.deserialize(response.body, 'String') as String ;
    } else {
      return null;
    }
  }
  /// Returns options for a server framework
  ///
  /// 
  Future<Map<String, CliOption>> genServersFrameworkGet(String framework,  {  bool justIgnoreThisFlag: true}) async {
    if (!justIgnoreThisFlag) {
      print('Why???   Just trust me, I only need this variable inside the mustache codegen template.');
      // This code may be removed as soon as dart accepts trailing spaces (has already been implemented).
    }
    Object postBody = null;

    // verify required params are set
    if(framework == null) {
     throw new ApiException(400, "Missing required param: framework");
    }

    // create path and map variables
    String path = "/gen/servers/{framework}".replaceAll("{format}","json").replaceAll("{" + "framework" + "}", framework.toString());

    // query params
    List<QueryParam> queryParams = [];
    Map<String, String> headerParams = {};
    Map<String, String> formParams = {};
    
    List<String> contentTypes = [];

    String contentType = contentTypes.length > 0 ? contentTypes[0] : "application/json";
    List<String> authNames = [];

    if(contentType.startsWith("multipart/form-data")) {
      bool hasFields = false;
      MultipartRequest mp = new MultipartRequest(null, null);
      
      if(hasFields)
        postBody = mp;
    }
    else {
          }

    var response = await apiClient.invokeAPI(path,
                                             'GET',
                                             queryParams,
                                             postBody,
                                             headerParams,
                                             formParams,
                                             contentType,
                                             authNames);

    if(response.statusCode >= 400) {
      throw new ApiException(response.statusCode, response.body);
    } else if(response.body != null) {
      return apiClient.deserialize(response.body, 'Map<String, CliOption>') as Map<String, CliOption> ;
    } else {
      return null;
    }
  }
  /// Generates a server library
  ///
  /// Accepts a &#x60;GeneratorInput&#x60; options map for spec location and generation options.
  Future<ResponseCode> genServersFrameworkPost(String framework,  {  bool justIgnoreThisFlag: true}) async {
    if (!justIgnoreThisFlag) {
      print('Why???   Just trust me, I only need this variable inside the mustache codegen template.');
      // This code may be removed as soon as dart accepts trailing spaces (has already been implemented).
    }
    Object postBody = null;

    // verify required params are set
    if(framework == null) {
     throw new ApiException(400, "Missing required param: framework");
    }

    // create path and map variables
    String path = "/gen/servers/{framework}".replaceAll("{format}","json").replaceAll("{" + "framework" + "}", framework.toString());

    // query params
    List<QueryParam> queryParams = [];
    Map<String, String> headerParams = {};
    Map<String, String> formParams = {};
    
    List<String> contentTypes = [];

    String contentType = contentTypes.length > 0 ? contentTypes[0] : "application/json";
    List<String> authNames = [];

    if(contentType.startsWith("multipart/form-data")) {
      bool hasFields = false;
      MultipartRequest mp = new MultipartRequest(null, null);
      
      if(hasFields)
        postBody = mp;
    }
    else {
          }

    var response = await apiClient.invokeAPI(path,
                                             'POST',
                                             queryParams,
                                             postBody,
                                             headerParams,
                                             formParams,
                                             contentType,
                                             authNames);

    if(response.statusCode >= 400) {
      throw new ApiException(response.statusCode, response.body);
    } else if(response.body != null) {
      return apiClient.deserialize(response.body, 'ResponseCode') as ResponseCode ;
    } else {
      return null;
    }
  }
  /// Gets languages supported by the server generator
  ///
  /// 
  Future<List<String>> genServersGet( {  bool justIgnoreThisFlag: true}) async {
    if (!justIgnoreThisFlag) {
      print('Why???   Just trust me, I only need this variable inside the mustache codegen template.');
      // This code may be removed as soon as dart accepts trailing spaces (has already been implemented).
    }
    Object postBody = null;

    // verify required params are set

    // create path and map variables
    String path = "/gen/servers".replaceAll("{format}","json");

    // query params
    List<QueryParam> queryParams = [];
    Map<String, String> headerParams = {};
    Map<String, String> formParams = {};
    
    List<String> contentTypes = [];

    String contentType = contentTypes.length > 0 ? contentTypes[0] : "application/json";
    List<String> authNames = [];

    if(contentType.startsWith("multipart/form-data")) {
      bool hasFields = false;
      MultipartRequest mp = new MultipartRequest(null, null);
      
      if(hasFields)
        postBody = mp;
    }
    else {
          }

    var response = await apiClient.invokeAPI(path,
                                             'GET',
                                             queryParams,
                                             postBody,
                                             headerParams,
                                             formParams,
                                             contentType,
                                             authNames);

    if(response.statusCode >= 400) {
      throw new ApiException(response.statusCode, response.body);
    } else if(response.body != null) {
      return apiClient.deserialize(response.body, 'List<String>') as List<String> ;
    } else {
      return null;
    }
  }
}
