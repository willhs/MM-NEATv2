cd ..
cd ..
java -jar dist/MM-NEATv2.jar runNumber:0 randomSeed:0 base:isolatedconflict maxGens:200 mu:100 io:true netio:true mating:true task:edu.utexas.cs.nn.tasks.mspacman.MsPacManPillsVsEdibleFromCornersMultitask highLevel:true infiniteEdibleTime:false pacManLevelTimeLimit:8000 pacmanInputOutputMediator:edu.utexas.cs.nn.tasks.mspacman.sensors.mediators.IICheckEachDirectionMediator trials:10 log:IsolatedConflict-MMR saveTo:MMR fs:false edibleTime:200 trapped:true mazePowerPillGhostMapping:data/pacman/PowerPillToGhostLocationMapping.txt removePillsNearPowerPills:true mmrRate:0.1
