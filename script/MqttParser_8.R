library(stringr)
library(sqldf)
library(plotly)
library(ggplot2)

Sys.setenv("plotly_username"="adrian")
Sys.setenv("plotly_api_key"="VRJrG4M0gP4r1XWfdDha")

u = "file://../out_java.csv"
tables <- read.csv(file=u, header=TRUE, sep=",")

allframeTransmissionTime = sqldf('select time, actions from tables where actions like "%TCPMSG-LEFT-TIME%"')

frameTransmissionTime = sqldf('select time, actions from tables where actions like "%TCPMSG-LEFT-TIME%" and actions like "%eth1%"')
foo <- data.frame(do.call('rbind', strsplit(as.character(frameTransmissionTime$actions),'|',fixed=TRUE)))
final <- data.frame(frameTransmissionTime$time, foo$X1, foo$X2)

frameTransmissionTime2 = sqldf('select time, actions from tables where actions like "%TCPMSG-LEFT-TIME%" and actions like "%eth3%"')
foo2 <- data.frame(do.call('rbind', strsplit(as.character(frameTransmissionTime2$actions),'|',fixed=TRUE)))
final2 <- data.frame(frameTransmissionTime2$time, foo2$X1, foo2$X2)

frameTransmissionTime3 = sqldf('select time, actions from tables where actions like "%TCPMSG-LEFT-TIME%" and actions like "%eth4%"')
foo3 <- data.frame(do.call('rbind', strsplit(as.character(frameTransmissionTime3$actions),'|',fixed=TRUE)))
final3 <- data.frame(frameTransmissionTime3$time, foo3$X1, foo3$X2)

#final_ad1 <- sqldf("select * from final where ")
tmpname <- list("time","event","transmision")
colnames(final) <- tmpname
colnames(final2) <- tmpname
colnames(final3) <- tmpname

hist(as.numeric(levels(final$transmision)[final$transmision]))
hist(as.numeric(levels(final2$transmision)[final2$transmision]))
hist(as.numeric(levels(final3$transmision)[final3$transmision]))

attach(mtcars)
par(mfrow=c(3,2)) 
plot(as.numeric(levels(final$transmision)[final$transmision]), type = "l", col="red", ylab = "Frame delivery time [s]", main="Floor 1 / eth1")
abline(h = mean(as.numeric(levels(final$transmision)[final$transmision])), col="purple")
hist(as.numeric(levels(final$transmision)[final$transmision]), xlab = "Frame delivery time [s]", main="Floor 1 / eth1")
plot(as.numeric(levels(final2$transmision)[final2$transmision]), type = "l", col="green", ylab = "Frame delivery time [s]", main="Floor 2 / eth2")
abline(h = mean(as.numeric(levels(final2$transmision)[final2$transmision])), col="blue")
hist(as.numeric(levels(final2$transmision)[final2$transmision]), xlab = "Frame delivery time [s]", main="Floor 2 / eth2")
plot(as.numeric(levels(final3$transmision)[final3$transmision]), type = "l", col="blue", ylab = "Frame delivery time [s]", main="Floor 3 / eth3")
abline(h = mean(as.numeric(levels(final3$transmision)[final3$transmision])), col="red")
hist(as.numeric(levels(final3$transmision)[final3$transmision]), xlab = "Frame delivery time [s]", main="Floor 3 / eth3")

#plot(as.numeric(levels(final$transmision)[final$transmision]), type = "l", col="red", ylab = "Frame delivery time [s]")
#lines(as.numeric(levels(final2$transmision)[final2$transmision]), type = "l",col="green")
#lines(as.numeric(levels(final3$transmision)[final3$transmision]), type = "l",col="blue")

#abline(h = mean(as.numeric(levels(final$transmision)[final$transmision])), col="purple")
#abline(h = mean(as.numeric(levels(final2$transmision)[final2$transmision])), col="blue")
#abline(h = mean(as.numeric(levels(final3$transmision)[final3$transmision])), col="red")

mean_eth1 = mean(as.numeric(levels(final$transmision)[final$transmision]))
mean_eth2 = mean(as.numeric(levels(final2$transmision)[final2$transmision]))
mean_eth3 = mean(as.numeric(levels(final3$transmision)[final3$transmision]))

## ilosc przesłanych pakietów na sek.
sek_start = 4.0
sek_end = 5.0
nrow(subset(final, final$time > sek_start & final$time < sek_end)) + nrow(subset(final2, final2$time > sek_start & final2$time < sek_end)) + nrow(subset(final3, final3$time > sek_start & final3$time < sek_end))