# Define here the models for your scraped items
#
# See documentation in:
# https://docs.scrapy.org/en/latest/topics/items.html

import scrapy


class BuffAnalyzerItem(scrapy.Item):
    # define the fields for your item here like:
    # name = scrapy.Field()
    title = scrapy.Field()
    factory_new = scrapy.Field()
    minimal_wear = scrapy.Field()
    field_tested = scrapy.Field()
    well_worn = scrapy.Field()
    battle_scarred = scrapy.Field()
