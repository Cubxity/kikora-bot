# Kikora-bot
Bot for solving exercises on [Kikora](https://feide-castor.kikora.no/beta/#/home).
This project was written in Kotlin and it utilizes [Kotlin Coroutines](https://github.com/Kotlin/kotlinx.coroutines) for multithreading and [kikora-api](https://github.com/quizdrop/kikora-api/) for interacting with Kikora.

> **NOTE:** This project is licensed under [GNU GPLv3](LICENSE) 

## Disclaimer
This project was written for educational purposes only.
I am **NOT** responsible for your actions using this project.
Kikora-bot is not affiliated with Kikora in any way.

## Usage
```bash
java -jar kikora-bot.jar --session-id <JSESSIONID> --tile <TILE> --container [CONTAINER]
```

## Performance
Kikora-bot runs the solver for each task concurrently.
The bot is very fast, the bottleneck here is most likely Kikora itself. 
The bot sends around 1-2 requests for each exercise.

## Strategies
**Generic:**
- Choice brute-forcing ***(1-2 requests)***

**Math:**
- Expression

**GeoGebra:**
- Geo auto
- Geo circles

So far, the bot could manage to solve **up to**  40-50% of the tasks in some container. 
