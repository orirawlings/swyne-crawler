all:
	javac -d bin/ -cp lib/core.jar:lib/htmlparser.jar:lib/rome-1.0RC2.jar:lib/rome-fetcher-1.0RC2.jar:lib/jdom.jar:lib/junit-4.5.jar:$(CLASSPATH) src/edu/iit/swyne/crawler/*.java \
	src/edu/iit/swyne/crawler/test/*.java \
	src/edu/iit/swyne/crawler/mock/*.java \
	src/edu/iit/swyne/crawler/client/*.java \
	src/edu/iit/swyne/crawler/server/*.java \
	src/edu/iit/swyne/crawler/extractor/*.java \
	src/edu/iit/swyne/crawler/experiment/*.java

crawl:
	java -cp bin:lib/htmlparser.jar:lib/rome-1.0RC2.jar:lib/rome-fetcher-1.0RC2.jar:lib/jdom.jar:lib/junit-4.5.jar:$(CLASSPATH) edu.iit.swyne.crawler.server.SwyneCrawlerServer config/swyne.xml &

build_corpus:
	java -cp bin:lib/htmlparser.jar:lib/rome-1.0RC2.jar:lib/rome-fetcher-1.0RC2.jar:lib/jdom.jar:lib/junit-4.5.jar:$(CLASSPATH) edu.iit.swyne.crawler.server.SwyneCrawlerServer config/corpus_build.xml &

experiment:
	java -d64 -Xms128m -Xmx512m -cp bin:lib/htmlparser.jar:lib/rome-1.0RC2.jar:lib/rome-fetcher-1.0RC2.jar:lib/jdom.jar:lib/junit-4.5.jar:$(CLASSPATH) edu.iit.swyne.crawler.experiment.Experiment data/corpus/corpusNews.xml data/corpus
