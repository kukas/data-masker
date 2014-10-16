from __future__ import print_function
from random import randint

print("Supr cupr madafaka generator pseudotajnych dat")
print()
print("Ze zadanych argumentu vygeneruje soubor.txt s daty a soubor_meta.txt s informacemi o jmenu sloupce, typu, delce a offsetu, masking rules si pak dopiste rucne, tomu nerozumim.")
print()
print("Syntax: datovyTyp(delka),datovyTyp(delka)")
print("Priklad: jmeno(20),prijmeni(20)")
print()
print("Moznosti: jmeno, prijmeni, rcislo, iban, telefon")
print()
print("Pravidla a parametry je nutno psat v poradi zleva pro kazdy sloupec, jinak to spadne nebo udela neco, co nechcete.")
print("Nic jineho tam nepiste, jinak to spadne.")
print("pozor na preklepy, jinak to taky spadne.")
print("Nehledejte v tom bugy, je jich tam plno (a vzdycky bude). Neopravim je.")
print()
print("pro Python 2")
print()
print("!!STAHNETE SI TO K SOBE A NEGENERUJTE TO NA SERVERU, NEBO SE TO CELY POS...!!")



vstup = raw_input("data v tabulce (to co je popsano vyse): ")
vstup = vstup.split(",")

pravidla = raw_input("maskovaci pravidla (po sloupcich, oddelit strednikem): ")
pravidla = pravidla.split(";")

parametry = raw_input("parametry (doted netusim jak to ma fungovat, ale to je jedno; oddelit strednikem): ")
parametry = parametry.split(";")

rules = []
params = []

for i in pravidla:
    rules.append(i)

for i in parametry:
    params.append(i)
    

radky = int(raw_input("pocet radku: "))

jmena = []
delky = []
offset = 1

for i in vstup:
    i = i.split("(")
    jmeno = i[0]
    delka = int(i[1][:-1])
    jmena.append(jmeno)
    delky.append(delka)
    
filename = raw_input("jmeno souboru: ")

meta = open("out\\"+filename+"_meta.txt","w")

for i in range(0,len(jmena),1):
    meta.write(jmena[i]+";"+"text"+";"+str(delky[i])+";"+str(offset)+";"+str(rules[i])+";"+params[i]+"\n")
    offset = offset + delky[i]

meta.close()

def jmeno(length):
    soubor = open("jmena.txt","r")
    jmena = []
    for i in soubor:
        jmena.append(i)

    poradi = randint(0,len(jmena)-1)
    jmeno = jmena[poradi]
    jmeno = jmeno.strip()
    if len(jmeno) > length:
        jmeno = jmeno[:length]

    while len(jmeno) < length:
        jmeno = jmeno+" "

    soubor.close()
    return jmeno

def prijmeni(length):
    soubor = open("prijmeni.txt","r")
    jmena = []
    for i in soubor:
        jmena.append(i)

    poradi = randint(0,len(jmena)-1)
    jmeno = jmena[poradi]
    jmeno = jmeno.strip()
    if len(jmeno) > length:
        jmeno = jmeno[:length]

    while len(jmeno) < length:
        jmeno = jmeno+" "

    soubor.close()
    return jmeno

def iban(length):
    if length < 3:
        length=3
        
    cislo = "CZ"
    for i in range(2,length,1):
        cifra = randint(0,9)
        cifra = str(cifra)
        cislo = cislo + cifra

    return cislo

def telefon(length):

    cislo = ""
    for i in range(0,length,1):
        cifra = randint(0,9)
        cifra = str(cifra)
        cislo = cislo + cifra

    return cislo

def rcislo(length):
    if length < 11:
        length=11

    den = randint(1,28)
    mesic = randint(1,12)
    rok = randint(10,99)
    pohlavi = randint(0,1)
    ocasek = randint(0,900)

    mesic = mesic + 50*pohlavi

    if den < 10:
        den = "0"+str(den)
    else:
        den = str(den)

    if mesic < 10:
        mesic = "0"+str(mesic)
    else:
        mesic = str(mesic)

    if rok < 10:
        rok = "0"+str(rok)
    else:
        rok = str(rok)

    ocasek = ocasek * 11

    if ocasek < 10:
        ocasek = "000"+str(ocasek)
    elif ocasek < 100:
        ocasek = "00"+str(ocasek)
    elif ocasek < 1000:
        ocasek = "0"+str(ocasek)
    else:
        ocasek = str(ocasek)

    cislo = rok+mesic+den+ocasek  
    cislo = int(cislo)
    

    while not divmod(cislo,11)[1] == 0:
        cislo = cislo + 1

    cislo = str(cislo)
    prvni = cislo[:6]
    prvni = prvni+"/"
    druha = cislo[6:]
    cislo = prvni+druha

    while len(cislo) < length:
        cislo = cislo + " "

    return cislo
    

db = open("out\\"+filename+".txt","w")

for i in range(radky):
    radek = ""
    
    for j in range(0,len(jmena),1):
        if jmena[j] == "jmeno":
            radek = radek + jmeno(delky[j])
        elif jmena[j] == "prijmeni":
            radek = radek + prijmeni(delky[j])
        elif jmena[j] == "iban":
            radek = radek + iban(delky[j])
        elif jmena[j] == "telefon":
            radek = radek + telefon(delky[j])
        elif jmena[j] == "rcislo":
            radek = radek + rcislo(delky[j])

    db.write(radek+"\n")

    if float(i)/1000 == int(float(i)/1000):
        print(str(i)+" radku z "+str(radky)+" zapsano.")

db.close()

print("hotovo")
print("soubor najdes ve slozce out/"+filename+".txt")
x = raw_input("stiskni enter pro ukonceni")
