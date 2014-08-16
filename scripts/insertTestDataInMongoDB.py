#!/usr/local/bin/python

import pymongo

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

def content(channel, program, start, end, type_program):
    content = {}
    content['channelName'] = channel
    content['programName'] = program
    content['start'] = start
    content['end'] = end
    content['typeProgram'] = type_program
    return content


tvChannel.insert(channel("itv4", "en"))
tvChannel.insert(channel("channel4", "en"))
tvChannel.insert(channel("bbc1", "en"))
tvChannel.insert(channel("bbc2", "en"))
tvChannel.insert(channel("bbc3", "en"))

tvContent.insert(content("itv4", "program1", 1, 2, "documentary"))
tvContent.insert(content("itv4", "program2", 2, 3, "news"))
tvContent.insert(content("itv4", "program3", 3, 4, "series"))

tvContent.insert(content("channel4", "program4", 3, 4, "documentary"))
tvContent.insert(content("channel4", "program5", 4, 5, "news"))
tvContent.insert(content("channel4", "program6", 5, 6, "series"))

tvContent.insert(content("bbc1", "program7", 1, 2, "documentary"))
tvContent.insert(content("bbc1", "program8", 2, 3, "news"))
tvContent.insert(content("bbc1", "program9", 3, 4, "series"))

tvContent.insert(content("bbc2", "program10", 8, 9, "documentary"))
tvContent.insert(content("bbc2", "program11", 9, 10, "news"))
tvContent.insert(content("bbc2", "program12", 10, 11, "series"))

tvContent.insert(content("bbc3", "program13", 15, 16, "documentary"))
tvContent.insert(content("bbc3", "program14", 16, 17, "news"))
tvContent.insert(content("bbc3", "program15", 17, 18, "series"))

