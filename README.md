Anomaly
========

## 2015/03/26

## ■ Updated
add package "mini.scrap.web.ScraperMini" and execute simple web-visit act.
- 0. Two files added `ProxyCheker.java` and `ScraperMini`.
- 1. Allow to visit a webpage periodically.
- 2. Enable to shutdown selenium driver in epxected period of time-wait.
- 3. Target on validating availability of proxies on list.
- 4. Two parameters added. `life` and `wait` to control life cycle of application and threads.


#web scraper for e-commerence website

Java version: 4.1
Eclipse vervion: 4.4(Luna)

Core package:
  1. main.scrape.web
  2. dumber.scrape.web
  3. utils.scrape.web
Examples: 
  1. fakepage.site.web
  2. ponparemall.site.web

Taget: to systematically scrape for products information through various of e-commerce websites

Steps to create a new webiste scraping:
 - 1. create package [A]
 - 2. create LinkPage class(s) and ContentPage class(s) in package [A]
   2.1 LinkPage
   2.1.1 inherits from class "ALinkPage"
   2.1.2 overrite class-method "retrieveLinks"
   2.2 ContentPage
   2.2.1 inherits from class "AContentPage"
   2.1.2 overrite class-method "retrieveContent"
 - 3 register created class(s) in order to class "Scraper"
   3.1 link-page listener "lplistener"
   3.2 content-page listener "cplistener"
 - 4 system parameter
   4.1 see class "Params"
 - NOTTE: please refer to project "fakepage" and "ponparemall" for extension details.


Scrape workflow:
  [urlroot, null] -> [urlstring_A, catestring_A] ->  
  ... -> [urlstring_Az, catestring_A]  -> [catestring_A, title, text]  
  |---- link page  scraping		-----|---------- content page scraping -----------|  
  |--       key-value Map   -------|------ key-value Map  -> JSONObject --------|


How to execute:  
`java -Dfile.encoding=UTF-8 -jar [JAR] -fname [PROXY] -thread [NTHREADS]`
