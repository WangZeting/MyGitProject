# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: https://docs.scrapy.org/en/latest/topics/item-pipeline.html


# useful for handling different item types with a single interface
from itemadapter import ItemAdapter

import json
import configparser
import time

from selenium import webdriver


class BuffanalyzerPipeline:

    def __init__(self):
        self.config = configparser.RawConfigParser()
        self.config.read('spiders/configuration.ini')
        self.start_time = None

    def open_spider(self, spider):
        self.start_time = time.time()
        if int(self.config.get('base', 'needLogin')) == 1:
            self.login()
        spider.driver = self.initialize()

    def close_spider(self, spider):
        spider.driver.close()
        end_time = time.time()
        print('总耗时: ', end_time - self.start_time)

    def process_item(self, item, spider):
        print(item)

    def login(self):
        driver_login = webdriver.Chrome()
        driver_login.get('https://buff.163.com/market/?game=csgo#tab=selling&page_num=1&category_group=knife')
        time.sleep(2)
        driver_login.switch_to.frame(driver_login.find_element_by_xpath('/html/body/div[7]/div[2]/div/div[1]/iframe'))
        driver_login.find_element_by_xpath('/html/body/div[2]/div[2]/div[2]/form/div/div[1]/a').click()
        driver_login.find_element_by_xpath('/html/body/div[2]/div[2]/div[2]/form/div/div[2]/div[1]/input').send_keys(
            self.config.get('base', 'userAccount'))
        driver_login.find_element_by_xpath('/html/body/div[2]/div[2]/div[2]/form/div/div[4]/div[2]/input[2]').send_keys(
            self.config.get('base', 'userPassword'))
        time.sleep(10)  # 滑块验证
        driver_login.find_element_by_xpath('/html/body/div[2]/div[2]/div[2]/form/div/div[7]/a').click()
        time.sleep(3)
        cookies = driver_login.get_cookies()
        print(cookies)
        fo = open('spiders/cookies.txt', 'w')
        fo.write(json.dumps(cookies))
        fo.close()
        driver_login.close()

    def initialize(self):
        chrome_options = webdriver.ChromeOptions()
        prefs = {"profile.managed_default_content_settings.images": 2}
        chrome_options.add_experimental_option("prefs", prefs)
        driver = webdriver.Chrome(chrome_options=chrome_options)
        driver.get('https://buff.163.com/market/?game=csgo#tab=selling&page_num=1&category_group=knife')
        driver.delete_all_cookies()
        fo = open('spiders/cookies.txt', 'r')
        cookies = json.loads(fo.read())
        fo.close()
        for item in cookies:
            cookie = {'name': item['name'], 'value': item['value']}
            driver.add_cookie(cookie)
        driver.get('https://buff.163.com/market/?game=csgo#tab=selling&page_num=1&category_group=knife')
        return driver
