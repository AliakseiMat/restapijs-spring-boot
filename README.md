# Rest Api JS Library

The goal of this lib is generating js models from Spring Boot controllers.

### JS Model Usage

The js model generating each startup time. After js file generated and included into html page, the js model could be used in javascript.

###### Suppose we have java model:
```sh
public class HelloModel {

    public long id;
    public String content;

    public HelloModel() {
    }
}
```

###### Then in javascript we can do the following:

* Get model from server
```sh
var hello = new MyNameSpace.Hello();
hello.id = 1;
hello.get(successCallback, errorCallback);
```
* Change instance
```sh
hello.content = "New Content"
hello.save(successCallback, errorCallback);
```
* Create new instance
```sh
var newHello = new MyNameSpace.Hello();
newHello.content = "Content from new model";
newHello.create(successCallback, errorCallback);
```
* Delete instance
```sh
hello.delete(successCallback, errorCallback);
```

The http request query arguments could be passed as third argument to get, save, create and delete methods:
```sh
hello.get(successCallback, errorCallback, {arg1:"value"});
```

### Annotation

###### Class level annotation:
```sh 
 @JsRestModelConfig(name="modelName", fileName="modelFileName.js", path="sub_path_to_file")
``` 
 This annotation annotate class controller which responsible for some java model class.
 
###### Method level annotation:
```sh 
 @JsRestModel(name="modelName")
``` 
 This annotation need to define the API methods used to GET, POST, PUT, DELETE java model.
 
### Spring MVC Controller

This is example of controller with annotations:
```sh
@JsRestModelConfig(name="Hello", fileName="hello_model.js", path="hello/")
@RestController
public class HelloRestController {

    @JsRestModel(name="Hello")
    @RequestMapping(path = "rest/hello/{id}", method  = RequestMethod.GET)
    public HelloModel getHelloModel(@PathVariable Integer id){
        /**/
    }

    @JsRestModel(name="Hello")
    @RequestMapping(path = "rest/hello", method  = RequestMethod.POST)
    public HelloModel createHelloModel(@RequestBody HelloModel helloModel){
        /**/
    }

    @JsRestModel(name="Hello")
    @RequestMapping(path = "rest/hello/{id}", method  = RequestMethod.PUT)
    public HelloModel updateHelloModel(@RequestBody HelloModel helloModel){
        /**/
    }

    @JsRestModel(name="Hello")
    @RequestMapping(path = "rest/hello/{id}", method  = RequestMethod.DELETE)
    public HelloModel deleteHelloModel(@RequestBody HelloModel helloModel){
        /**/
    }
}
```
 
 Restapijs library used @RequestMapping annotation to define the path and method for generated js model.
 
### Spring application properties

 The following properties used to set up lib:
```sh
restjs.js.path=src/main/resources/static/js/models/
restapijs.js.namespace=MyNameSpace
restjs.disable=false
```

restjs.js.path - the basic path where generated js file will be placed

restapijs.js.namespace - javascript namespace for js model

restjs.disable - disable generating js models 
