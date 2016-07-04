# import a dataset
from sklearn import datasets
iris = datasets.load_iris()

X = iris.data
y = iris.target

from sklearn.cross_validation import train_test_split
# 150 in iris dataset, 75 for training, 75 for testing
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=.5)

# # decision tree
# from sklearn import tree
# my_classifier = tree.DecisionTreeClassifier()
# K-nearest neighbors
from sklearn.neighbors import KNeighborsClassifier
my_classifier = KNeighborsClassifier()

my_classifier.fit(X_train, y_train)

predictions = my_classifier.predict(X_test)
print predictions

from sklearn.metrics import accuracy_score
print accuracy_score(y_test, predictions)
