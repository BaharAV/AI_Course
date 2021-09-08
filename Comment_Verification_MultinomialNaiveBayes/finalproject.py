import pandas as pd
from collections import defaultdict
import re

def seprate(total):
    words = []
    if (pd.isnull(total) == False):
        total = re.sub(r'[^\w\s]',' ', total)
        # words= [t for t in total.split() if t not in stopwords]
        words = total.split()
        return words
    return words

train = pd.read_csv('train.csv', encoding='utf-8')
train['total'] = train['comment'] + " " + train['title']
test = pd.read_csv('test.csv', encoding='utf-8')
test['total'] = test['comment'] + " " + test['title']

#chance of fake and notfake
fakes = train[train['verification_status'] == 1] #all fake comments
notfakes = train[train['verification_status'] == 0] #all notfake comments
chancefake = len(fakes) / len(train)
chancenotfake = len(notfakes) / len(train)

# stop_words= []
# with open("stopwords-fa.txt" ,encoding = 'utf-8' ) as f:
#     stop_words = f.read().splitlines()
# stopwords = stop_words [1:]

savefakes = [] #all words of a fake comment seprated
savenotfakes = []  #all words of a not fake comment seprated

for cmnt in fakes['total']:
    words = seprate(cmnt)
    savefakes.append(words)
for cmnt in notfakes['total']:
    words = seprate(cmnt)
    savenotfakes.append(words)

fakecount = 0 #number of all fake words
notfakecount = 0 #number of all notfake words
alluniwords = set() #all unique word -> set
fakescountdic = defaultdict(lambda: 1)
notfakescountdic = defaultdict(lambda: 1)

for words in savefakes:
    fakecount += len(words)
    for word in words:
        fakescountdic[word] += 1
        alluniwords.add(word)
for words in savenotfakes:
    notfakecount += len(words)
    for word in words:
        notfakescountdic[word] += 1
        alluniwords.add(word)
V = len(alluniwords)

chanceinfake = defaultdict(lambda: 1 / (fakecount + V))
chanceinnotfake = defaultdict(lambda: 1 / (notfakecount + V))

for word in alluniwords:
    chanceinfake[word] = (fakescountdic[word] + 1) / (fakecount + V)
    chanceinnotfake[word] = (notfakescountdic[word] + 1) / (notfakecount + V)

finalres = []
for cmnt in test['total']: #in test comments
    words = seprate(cmnt)
    isfake = chancefake * 10E100
    isnotfake = chancenotfake * 10E100
    for t in words: #in words of a comment
        isfake *= chanceinfake[t]
        isnotfake *= chanceinnotfake[t]
    if (isfake > isnotfake):
        res = 1
    else:
        res = 0
    finalres.append(res)

finalresfile = pd.DataFrame({"id": test['id'], "verification_status": finalres}).to_csv("ans.csv",index=False)
print(finalres)
print("done!")

