cd ..
cd ..
java -jar dist/MM-NEATv2.jar runNumber:%1 randomSeed:%1 base:vizdoommultideathmatch trials:10 maxGens:100 mu:50 io:true netio:true mating:true task:edu.utexas.cs.nn.tasks.vizdoom.VizDoomMultiDeathMatchTask cleanOldNetworks:true fs:false noisyTaskStat:edu.utexas.cs.nn.util.stats.Average log:MultiDeathMatch-Control saveTo:Control gameWad:freedoom2.wad doomEpisodeLength:2100