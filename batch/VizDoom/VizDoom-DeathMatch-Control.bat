cd ..
cd ..
java -jar dist/MM-NEATv2.jar runNumber:%1 randomSeed:%1 base:vizdoomdeathmatch trials:10 maxGens:100 mu:50 io:true netio:true mating:true task:edu.utexas.cs.nn.tasks.vizdoom.VizDoomDeathMatchTask cleanOldNetworks:true fs:false noisyTaskStat:edu.utexas.cs.nn.util.stats.Average log:DeathMatch-Control saveTo:Control gameWad:freedoom2.wad doomEpisodeLength:4200