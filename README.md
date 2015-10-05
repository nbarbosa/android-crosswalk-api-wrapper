# Crosswalk Embedding API Wrapper App
This Android app is a wrapper for the [Crosswalk Embedding API](https://crosswalk-project.org/documentation/apis/embedding_api.html) with error handling, localization, page load and navigation control.

The app is built with [Fragments](http://developer.android.com/guide/components/fragments.html). There is also a fallback fragment with default OS WebView.

#### Project Dependencies
In order to build the project and debug it, Crosswalk Canary must be installed on your local maven repository.

Download Crosswalk Canary AAR:

    wget https://download.01.org/crosswalk/releases/crosswalk/android/canary/15.44.377.0/crosswalk-15.44.377.0.aar

Install Crosswalk Canary AAR:

    mvn install:install-file -DgroupId=org.xwalk -DartifactId=xwalk_core_library_canary -Dversion=15.44.377.0 -Dpackaging=aar -Dfile=crosswalk-15.44.377.0.aar -DgeneratePom=true

Import project into Android Studio.

## License
Copyright 2015 Natã Barbosa.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.





