#!/usr/local/bin/python

import pymongo
import datetime
from pymongo import MongoClient


client = MongoClient('mongodb://localhost:27017/')
db = client['freeview']

tvChannel = db['tvChannel']
tvChannel.drop()

tvContent = db['tvContent']
tvContent.drop()

def channel(name, language):
    channel = {}
    channel['name'] = name
    channel['language'] = language
    return channel

def content(channel, category, start, end):
    content = {}
    content['channel'] = channel
    content['category'] = category
    content['serie'] = {}
    content['serie']['episodeNumber'] = "1"
    content['serie']['seasonNumber'] = "2"
    content['serie']['description'] = "3"
    content['serie']['totalNumber'] = "4"
    content['serie']['serieTitle'] = "5"
    content['serie']['episodeTitle'] = "6"
    content['startTime'] = datetime.datetime.now()
    content['endTime'] = datetime.datetime.now()
    return content





tvChannel.insert(channel("itv4", "en"))
tvChannel.insert(channel("channel4", "en"))
tvChannel.insert(channel("bbc1", "en"))
tvChannel.insert(channel("bbc2", "en"))
tvChannel.insert(channel("bbc3", "en"))

tvContent.insert(content("ITV4","c1",datetime.datetime.now(), datetime.datetime.now()))
