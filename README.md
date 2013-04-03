TaskLocalRandom
===============

TaskLocalRandom is a research project enabling safe partitionning of pseudorandom stream in Java Tasks frameworks, such as Fork/Join or Executors.  
It tackles the lack from ThreadLocalRandom that assigns the same pseudorandom stream to every task run in the same worker thread. 
