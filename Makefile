# Variables
SRC_DIR = src
TEST_DIR = test
BUILD_DIR = build
CLASSES_DIR = $(BUILD_DIR)/cls
DOC_DIR = doc
UML_DIR = $(DOC_DIR)/uml
COVERAGE_DIR = $(BUILD_DIR)/coverage
JACOCO_AGENT = lib/jacocoagent.jar
JACOCO_CLI = lib/jacococli.jar
CLASSPATH = $(CLASSES_DIR):lib/*

# Find all source and test Java files
SRC_FILES = $(shell find $(SRC_DIR) -name "*.java")
UNIT_TEST_FILES = $(shell find $(TEST_DIR)/unit -name "*.java")
FUNCTIONAL_TEST_FILES = $(shell find $(TEST_DIR)/functional -name "*.java")
ALL_TEST_FILES = $(UNIT_TEST_FILES) $(FUNCTIONAL_TEST_FILES)

# Targets
.PHONY: all clean compile doc clean-doc tests

all: compile

# Clean the build directory
clean:
	rm -rf $(BUILD_DIR)

# Compile all source and test files
compile: clean
	mkdir -p $(BUILD_DIR)
	mkdir -p $(CLASSES_DIR)
	javac -d $(CLASSES_DIR) -cp $(CLASSPATH) $(SRC_FILES) $(ALL_TEST_FILES)

# Run all tests
tests: func-tests unit-tests

# Run tests with coverage
coverage: compile
	@echo "Running tests with coverage..."
	@mkdir -p $(COVERAGE_DIR)
	@# Run unit tests with JaCoCo agent
	@for test in $(UNIT_TEST_FILES:.java=); do \
		test_class=$$(echo $$test | sed 's/test\/unit\///' | sed 's/\//./g'); \
		java -javaagent:$(JACOCO_AGENT)=destfile=$(COVERAGE_DIR)/jacoco-unit.exec,append=true \
			-cp $(CLASSPATH):lib/junit-4.13.2.jar:lib/hamcrest-2.2.jar:. \
			org.junit.runner.JUnitCore $$test_class || true; \
	done
	@# Run functional tests with JaCoCo agent
	@for test in $(FUNCTIONAL_TEST_FILES:.java=); do \
		test_class=$$(echo $$test | sed 's/test\/functional\///' | sed 's/\//./g'); \
		java -javaagent:$(JACOCO_AGENT)=destfile=$(COVERAGE_DIR)/jacoco-func.exec,append=true \
			-cp $(CLASSPATH):lib/junit-4.13.2.jar:lib/hamcrest-2.2.jar:. \
			org.junit.runner.JUnitCore $$test_class || true; \
	done
	@# Generate HTML report
	@echo "Generating coverage report..."
	@java -jar $(JACOCO_CLI) report $(COVERAGE_DIR)/*.exec \
		--classfiles $(CLASSES_DIR) \
		--sourcefiles $(SRC_DIR) \
		--html $(COVERAGE_DIR)/report \
		--xml $(COVERAGE_DIR)/report.xml \
		--csv $(COVERAGE_DIR)/report.csv
	@echo ""
	@echo "=========================================="
	@echo "Coverage Summary"
	@echo "=========================================="
	@awk -F',' 'NR==1 {next} \
		{missed_inst+=$$4; covered_inst+=$$5; \
		 missed_branch+=$$6; covered_branch+=$$7; \
		 missed_line+=$$8; covered_line+=$$9; \
		 missed_method+=$$10; covered_method+=$$11; \
		 missed_class+=$$12; covered_class+=$$13} \
		END { \
			total_inst=missed_inst+covered_inst; \
			total_branch=missed_branch+covered_branch; \
			total_line=missed_line+covered_line; \
			total_method=missed_method+covered_method; \
			total_class=missed_class+covered_class; \
			printf "Instructions: %d/%d (%.1f%%)\n", covered_inst, total_inst, (total_inst>0?covered_inst*100/total_inst:0); \
			printf "Branches:     %d/%d (%.1f%%)\n", covered_branch, total_branch, (total_branch>0?covered_branch*100/total_branch:0); \
			printf "Lines:        %d/%d (%.1f%%)\n", covered_line, total_line, (total_line>0?covered_line*100/total_line:0); \
			printf "Methods:      %d/%d (%.1f%%)\n", covered_method, total_method, (total_method>0?covered_method*100/total_method:0); \
			printf "Classes:      %d/%d (%.1f%%)\n", covered_class, total_class, (total_class>0?covered_class*100/total_class:0); \
		}' $(COVERAGE_DIR)/report.csv
	@echo "=========================================="
	@echo "Detailed report: $(COVERAGE_DIR)/report/index.html"
	@echo "=========================================="

unit-tests: compile
	@echo "Running all unit tests..."
	@for test in $(UNIT_TEST_FILES:.java=); do \
		test_name=$$(basename $$test); \
		test_class=$$(echo $$test | sed 's/test\/unit\///' | sed 's/\//./g'); \
		echo "Running JUnit test $$test_name... $$test_class"; \
		java -cp $(CLASSPATH):lib/junit-4.13.2.jar:lib/hamcrest-2.2.jar:. \
			org.junit.runner.JUnitCore $$test_class || exit 1; \
	done

func-tests: compile
	@echo "Running functional tests..."
	@for test in $(FUNCTIONAL_TEST_FILES:.java=); do \
		test_name=$$(basename $$test); \
		test_class=$$(echo $$test_name | sed 's/\.java//'); \
		echo "Running functional test $$test_class..."; \
		java -cp $(CLASSPATH):lib/junit-4.13.2.jar:lib/hamcrest-2.2.jar:. \
			org.junit.runner.JUnitCore $$test_class || exit 1; \
	done
	@echo "All tests completed."

# run the renderer
run: compile
	java -cp $(CLASSPATH) renderer.gui.GUIApp

# Generate Javadoc
doc: clean-doc
	mkdir -p ${DOC_DIR}
	javadoc -d ${DOC_DIR} -sourcepath ${SRC_DIR} ${SRC_FILES}

# clean the doc directory
clean-doc:
	rm -rf ${DOC_DIR}

# create plant UML file
createUML: compile
	java -jar lib/plantuml-dep-cli-1.4.0.jar -b src/ -dp "^renderer.(algebra|core|controller).*" -o $(UML_DIR)/ClassDiagramWithoutMembers.puml
	java -cp $(CLASSPATH) renderer.doc.CreateClassDiagram -dp "^renderer.(algebra|core|controller).*"

# draw plant UML File 
drawUML:
	export PLANTUML_LIMIT_SIZE=8192
	java -jar lib/plantuml-1.2025.3.jar net.sourceforge.plantuml.ant.PlantUmlTask $(UML_DIR)

create-drawUML:createUML drawUML