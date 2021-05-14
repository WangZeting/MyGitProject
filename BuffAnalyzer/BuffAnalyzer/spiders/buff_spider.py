import scrapy
import configparser
from BuffAnalyzer.items import BuffAnalyzerItem


class BuffSpiderSpider(scrapy.Spider):
    name = 'buff_spider'
    allowed_domains = ['buff.163.com']

    def __init__(self):
        self.config = configparser.RawConfigParser()
        self.config.read('spiders/configuration.ini')
        self.start_urls = []
        self.driver = None
        self.tart_list = []

    def start_requests(self):
        for key, value in self.config.items('urls'):
            self.start_urls.append(value)
        if int(self.config.get('base', 'amountNeed')) == 1:
            for url in self.start_urls:
                yield scrapy.Request(url, callback=self.parse_single, dont_filter=True)
        else:
            for url in self.start_urls:
                yield scrapy.Request(url, callback=self.parse_multiple, dont_filter=True)

    def parse(self, response):
        pass

    def parse_single(self, response):
        target_item = BuffAnalyzerItem()
        target_item['title'] = response.xpath('/html/body/div[5]/div[1]/div[4]/div[1]/ul/li[1]/h3/a/@title').get()[
                               0:-7]
        ul = response.xpath('/html/body/div[5]/div[1]/div[4]/div[1]/ul/li')
        for li in ul:
            if li.xpath('./span/text()').get() == '崭新出厂':
                decimal = '' if li.xpath('./p/strong/small/text()').get() is None else li.xpath(
                    './p/strong/small/text()').get()
                target_item['factory_new'] = float(li.xpath('./p/strong/text()').get()[2:] + decimal)
            if li.xpath('./span/text()').get() == '略有磨损':
                decimal = '' if li.xpath('./p/strong/small/text()').get() is None else li.xpath(
                    './p/strong/small/text()').get()
                target_item['minimal_wear'] = float(li.xpath('./p/strong/text()').get()[2:] + decimal)
            if li.xpath('./span/text()').get() == '久经沙场':
                decimal = '' if li.xpath('./p/strong/small/text()').get() is None else li.xpath(
                    './p/strong/small/text()').get()
                target_item['field_tested'] = float(li.xpath('./p/strong/text()').get()[2:] + decimal)
            if li.xpath('./span/text()').get() == '破损不堪':
                decimal = '' if li.xpath('./p/strong/small/text()').get() is None else li.xpath(
                    './p/strong/small/text()').get()
                target_item['well_worn'] = float(li.xpath('./p/strong/text()').get()[2:] + decimal)
            if li.xpath('./span/text()').get() == '战痕累累':
                decimal = '' if li.xpath('./p/strong/small/text()').get() is None else li.xpath(
                    './p/strong/small/text()').get()
                target_item['battle_scarred'] = float(li.xpath('./p/strong/text()').get()[2:] + decimal)
        yield target_item

    def parse_multiple(self, response):
        page_title = response.xpath('/html/head/title/text()').get()
        target_name = page_title[0:-24]
        if page_title == 'CS:GO饰品市场_网易BUFF饰品交易平台':
            item = BuffAnalyzerItem()
            item['title'] = response.xpath('/html/body/div[5]/div[1]/div[4]/div[1]/ul/li[1]/h3/a/@title').get()[0:-7]
            self.tart_list.append(item)
            ul = response.xpath('/html/body/div[5]/div[1]/div[4]/div[1]/ul/li')
            for li in ul:
                yield scrapy.Request('https://buff.163.com' + li.xpath('./a/@href').get(), callback=self.parse_multiple,
                                     dont_filter=True)
        elif '崭新出厂' in page_title:
            for target in self.tart_list:
                if target_name == target['title']:
                    target['factory_new'] = {}
                    tr = response.xpath('/html/body/div[6]/div/div[6]/table/tbody/tr[@id]')
                    for i in range(int(self.config.get('base', 'amountNeed'))):
                        target['factory_new'][tr[i].xpath('./td[3]/div/div[1]/div[1]/text()').get()[4:]] = float(
                            tr[i].xpath('./td[5]/div[1]/p/span/text()').get()[3:-1])
                    if len(target) == 6:
                        yield target
        elif '略有磨损' in page_title:
            for target in self.tart_list:
                if target_name == target['title']:
                    target['minimal_wear'] = {}
                    tr = response.xpath('/html/body/div[6]/div/div[6]/table/tbody/tr[@id]')
                    for i in range(int(self.config.get('base', 'amountNeed'))):
                        target['minimal_wear'][tr[i].xpath('./td[3]/div/div[1]/div[1]/text()').get()[4:]] = float(
                            tr[i].xpath('./td[5]/div[1]/p/span/text()').get()[3:-1])
                    if len(target) == 6:
                        yield target
        elif '久经沙场' in page_title:
            for target in self.tart_list:
                if target_name == target['title']:
                    target['field_tested'] = {}
                    tr = response.xpath('/html/body/div[6]/div/div[6]/table/tbody/tr[@id]')
                    for i in range(int(self.config.get('base', 'amountNeed'))):
                        target['field_tested'][tr[i].xpath('./td[3]/div/div[1]/div[1]/text()').get()[4:]] = float(
                            tr[i].xpath('./td[5]/div[1]/p/span/text()').get()[3:-1])
                    if len(target) == 6:
                        yield target
        elif '破损不堪' in page_title:
            for target in self.tart_list:
                if target_name == target['title']:
                    target['well_worn'] = {}
                    tr = response.xpath('/html/body/div[6]/div/div[6]/table/tbody/tr[@id]')
                    for i in range(int(self.config.get('base', 'amountNeed'))):
                        target['well_worn'][tr[i].xpath('./td[3]/div/div[1]/div[1]/text()').get()[4:]] = float(
                            tr[i].xpath('./td[5]/div[1]/p/span/text()').get()[3:-1])
                    if len(target) == 6:
                        yield target
        elif '战痕累累' in page_title:
            for target in self.tart_list:
                if target_name == target['title']:
                    target['battle_scarred'] = {}
                    tr = response.xpath('/html/body/div[6]/div/div[6]/table/tbody/tr[@id]')
                    for i in range(int(self.config.get('base', 'amountNeed'))):
                        target['battle_scarred'][tr[i].xpath('./td[3]/div/div[1]/div[1]/text()').get()[4:]] = float(
                            tr[i].xpath('./td[5]/div[1]/p/span/text()').get()[3:-1])
                    if len(target) == 6:
                        yield target
