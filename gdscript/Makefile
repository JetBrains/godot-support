download-docs:
	wget -O - https://github.com/godotengine/godot/archive/master.tar.gz | tar -xz --strip=2 godot-master/doc/classes
	cp build_files/@GdScript.xml classes/@GdScript.xml

refresh-classes:
	php classParser.php

zip:
	zip -r GD_SDK-0.4.1.7z classesGd

generate: download-docs refresh-classes zip
