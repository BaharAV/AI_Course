import pandas as pd
import re
from nltk.classify import SklearnClassifier
from sklearn.naive_bayes import MultinomialNB
from collections import defaultdict


def seprate(total):
    words = []
    if (pd.isnull(total) == False):
        total = re.sub(r'[^\w\s]', ' ', total)
        # words= [t for t in total.split() if t not in stopwords]
        words = total.split()
        return words
    return words


# stop_words= []
# with open("stopwords-fa.txt" ,encoding = 'utf-8' ) as f:
#     stop_words = f.read().splitlines()
# stopwords = stop_words [1:]

train = pd.read_csv('train.csv', encoding='utf-8')
train['total'] = train['comment'] + " " + train['title']
test = pd.read_csv('test.csv', encoding='utf-8')
test['total'] = test['comment'] + " " + test['title']

trainer = []
for indices, row in train.iterrows():
    save = seprate(row['total'])
    cntr = defaultdict(lambda: 1)
    for t in save:
        cntr[t] += 1
    trainer.append((cntr, row['verification_status']))
print("train done!")

tester = []
for indices, row in test.iterrows():
    save = seprate(row['total'])
    cntr = defaultdict(lambda: 1)
    for t in save:
        cntr[t] += 1
    tester.append(cntr)
print("test done!")

clf = SklearnClassifier(MultinomialNB()).train(trainer)
finalres = clf.classify_many(tester)

pd.DataFrame({"id": test['id'], "verification_status": finalres}).to_csv("ans.csv", index=False)
print("done!")
