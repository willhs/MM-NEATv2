cd ..
cd ..
java -jar dist/MM-NEATv2.jar runNumber:%1 randomSeed:%1 base:picbreeder trials:1 mu:16 maxGens:500 io:true netio:true mating:true fs:false task:edu.utexas.cs.nn.tasks.picbreeder.PicbreederTask log:Picbreeder-Control saveTo:Control allowMultipleFunctions:true ftype:0 watch:false netChangeActivationRate:0.3 cleanFrequency:-1 recurrency:false saveAllChampions:true cleanOldNetworks:false logTWEANNData:false logMutationAndLineage:true ea:edu.utexas.cs.nn.evolution.selectiveBreeding.SelectiveBreedingEA imageWidth:2000 imageHeight:2000
