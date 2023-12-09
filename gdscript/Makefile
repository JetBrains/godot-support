update:
	wget -O - https://github.com/godotengine/godot/archive/master.tar.gz | tar -xz --strip=2 godot-master/editor/icons
	mv icons/* src/main/resources/icons/godot_editor/
	php operandParser.php
	php annotationParser.php
