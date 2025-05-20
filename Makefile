# Makefile para proyecto Gradle + Gson

# Ajusta si tienes otra versión de Gson o carpeta distinta
GSON_JAR   := libs/gson-2.13.1.jar
MAIN_CLASS := Domini.DriverDomini

# Encuentra el primer JAR generado en build/libs
JAR_FILE   := build/libs/scrabble-1.0.jar

.PHONY:  clean  build build-tests  jar run run-jar

# 1) Compila solo las clases principales
build:
	@echo ">>> Compilant..."
	./gradlew build -x test
	@echo ">>> Projecte compilat..."


# 2) Compila también los tests (sin ejecutarlos)
build-tests: build
	@echo ">>> Compilant amb tests..."
	./gradlew build
		@echo ">>> Projecte compilat i testejat ..."



# 4) Genera el JAR estándar (no incluye dependencias)
jar: build
	@echo ">>> Generando JAR..."
	./gradlew jar

# 5) Arranca con -cp
run: build
	@echo ">>> Ejecutando con -cp..."
	java -cp build/classes/java/main:$(GSON_JAR) $(MAIN_CLASS)

# 6) Arranca con java -jar sobre el JAR generado
run-jar: jar
ifneq ($(JAR_FILE),)
	@echo ">>> Executant amb java -jar $(JAR_FILE)..."
	java -jar $(JAR_FILE)
else
	$(error No se encontró ningún JAR en build/libs/)
endif

# 7) Limpia todo
clean:
	@echo ">>> Limpiando proyecto..."
	./gradlew clean
