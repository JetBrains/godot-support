download-docs:
	#wget -O - https://github.com/godotengine/godot/archive/master.tar.gz | tar -xz
	wget -O - https://github.com/godotengine/godot/archive/master.tar.gz | tar -xz --strip=2 godot-master/doc/classes

refresh-classes:
	mkdir classesGd || true
	php classParser.php
	#php templateParser.php
	cp build_files/@GdScript.xml classes/@GdScript.xml

zip:
	zip -r GD_SDK.7z classesGd

generate: download-docs refresh-classes zip
