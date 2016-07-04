from sklearn import tree

features = [[140, 1], [130, 1], [150, 0], [170, 0]]  # smooth=1, bumpy=0
labels = [0, 0, 1, 1]  # apple=0, orange=1

clf = tree.DecisionTreeClassifier()
clf = clf.fit(features, labels)
print(clf.predict([160, 0]))
