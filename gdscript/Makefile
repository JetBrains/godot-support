download-docs:
	wget -O - https://github.com/godotengine/godot/archive/master.tar.gz | tar -xz --strip=2 godot-master/doc/classes

refresh-classes:
	php classParser.php
	mv ./classesGd/* ./src/main/resources/classes_test

generate: download-docs refresh-classes
