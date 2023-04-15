download-docs:
	wget -O - https://github.com/godotengine/godot/archive/master.tar.gz | tar -xz

refresh-classes:
	rm -R classesGd || true
	mkdir classesGd || true
	mkdir classesGd/modules || true
	php classParser.php
	#php templateParser.php

zip:
	zip -r GD_SDK.7z classesGd

generate: download-docs refresh-classes zip

update: generate
	wget -O - https://github.com/godotengine/godot/archive/master.tar.gz | tar -xz --strip=2 godot-master/editor/icons
	mv icons/* src/main/resources/icons/godot_editor/
	php operandParser.php
	php annotationParser.php
