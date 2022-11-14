download-docs:
	#wget -O - https://github.com/godotengine/godot/archive/master.tar.gz | tar -xz
	php templateParser.php

refresh-classes:
	php classParser.php
	php templateParser.php
	cp build_files/@GdScript.xml classes/@GdScript.xml

zip:
	zip -r GD_SDK-0.4.1.7z classesGd

generate: download-docs refresh-classes zip
