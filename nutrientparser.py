import string
import sys
from json import loads
from re import sub

columnSeparator = "|"

"""
Returns true if a file ends in .json
"""
def isJson(f):
    return len(f) > 5 and f[-5:] == '.json'


"""
Parses a single json file. Currently, there's a loop that iterates over each
item in the data set. 
"""
def parseJson(json_file):
    with open(json_file, 'r') as f:
        items = loads(f.read())['SRLegacyFoods']

        # Nutrition table
        f = open("nutrition.dat", "a")
        for item in items:
            description = item.get('description')
            if description is not str:
                description = str(description)
            line = description + columnSeparator

            energy = '0'
            protein = '0'
            fat = '0'
            carbs = '0'
            sodium = '0'
            potassium = '0'

            if item['foodNutrients'] is not None:
                for nutrient in item['foodNutrients']:
                    if nutrient['nutrient']['name'] == 'Energy' and nutrient['nutrient']['unitName'] == 'kcal':
                        energy = str(nutrient['amount'])
                    if nutrient['nutrient']['name'] == 'Protein':
                        protein = str(nutrient['amount'])
                    if nutrient['nutrient']['name'] == 'Total lipid (fat)':
                        fat = str(nutrient['amount'])
                    if nutrient['nutrient']['name'] == 'Carbohydrate, by difference':
                        carbs = str(nutrient['amount'])
                    if nutrient['nutrient']['name'] == 'Sodium, Na':
                        sodium = str(nutrient['amount'])
                    if nutrient['nutrient']['name'] == 'Potassium, K':
                        potassium = str(nutrient['amount'])

            f.write(line + energy + columnSeparator + protein + columnSeparator + fat +
                     columnSeparator + carbs + columnSeparator + sodium + columnSeparator + potassium + "\n")
        f.close()


"""
Loops through each json file provided on the command line and passes each file
to the parser
"""
def main(argv):
    if len(argv) < 2:
        print >> sys.stderr, 'Usage: python skeleton_json_parser.py <path to json files>'
        sys.exit(1)
    # loops over all .json files in the argument
    for f in argv[1:]:
        if isJson(f):
            parseJson(f)
            print ("Success parsing " + f)

if __name__ == '__main__':
    main(sys.argv)
