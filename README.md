tag-reader
==========

Given XML stream, returns content or (if contains) child nodes for specified element.


Basic Ecample
=============

``` java
...
String xml = "<start><output>Content</output></start>";
Result<String, String> element = new XmlTagReader().tag("output").textContent().first();
String content = element.get("output"); // "Content"

....
String xml = "<start><output><errorCode>1</errorCode><errorDescription>SQL Exception</errorDescription></output></start>";
List<Result<String, String>> elements = new XmlTagReader().tag("output").withChildren().first();
elements.get(0).get("errorCode"); //1
elements.get(0).get("errorDescription"); //"SQL Exception"

....
String xml = "<start><customer><name>Foo</name><id>1</id><restOfPersonalData>....</restOfPersonalData></customer>" + 
				"<customer>Another Customer</customer></start>";
List<Result<String, String>> elements = new XmlTagReader().tag("output").withChildren("name").first(); //return only name of first customer
elements.get(0).get("name"); //"Foo"
elements.get(0).get("id"); // ""
elements.size(); // 1
```


Dependency
==========

You have to add to your runtime classpath library: JSR-173 API and some its implementation.


License
=======

Copyright 2014 Kamil Zurek

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.