df = data.frame(drybulbweather=k$tempbulboseco, wetbulbweather=k$tempbulboumido, 
                relativehumidity=k$umidaderelativa, atmosphericpressure=k$pressaoatmestacao, 
                windspeed=k$velocidadevento, cloudiness=k$nebulosidade, 
                winddirection=k$direcaovento)

df$departuredelay <- NA
df$departuredelay<-ifelse(!is.na(k$tempoatrasopartida), 1, 0)

dfbin = data.frame(bindrybulbweather=k$bintempbulboseco, binwetbulbweather=k$bintempbulboumido, 
                   binrelativehumidity=k$binumidaderelativa, 
                   binatmosphericpressure=k$binpressaoatmestacao, 
                   binwindspeed=k$binvelocidadevento, bincloudiness=k$binnebulosidade, 
                   codwinddirection=k$direcaoventocod)

dfbin$departuredelay <- NA
dfbin$departuredelay<-ifelse(!is.na(k$tempoatrasopartida), 1, 0)