download-docs:
	wget -O - https://github.com/godotengine/godot/archive/master.tar.gz | tar -xz --strip=2 godot-master/doc/classes

refresh-classes:
	php classParser.php

generate: download-docs refresh-classes
