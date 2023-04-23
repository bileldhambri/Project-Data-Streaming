import random
import csv
import time
from datetime import datetime, timedelta


companies = ['Amazon', 'Microsoft', 'Google']
current_date = datetime.now().strftime('%m/%d/%Y %H:%M:%S')


with open('stock_data.csv', mode='w') as file:
    writer = csv.writer(file)
    writer.writerow(['Date','Company','Price', 'Change Point', 'Change Percentage', 'Total Volume'])

while True:
    with open('stock_data.csv', mode='a') as file:
        writer = csv.writer(file)
        for company in companies:
            price = round(random.uniform(1000, 5000), 2)
            change_point = round(random.uniform(-50, 50), 2)
            change_percentage = round(change_point / price * 100, 2)
            total_volume = round(random.uniform(1, 10) * 1000000, 2)
            writer.writerow([current_date, company, price, change_point, change_percentage, f"{total_volume:.2f}M"])
            print(f"{current_date}: {company} - Price: {price}, Change Point: {change_point}, Change Percentage: {change_percentage}%, Total Volume: {total_volume:.2f}M")
        time.sleep(random.uniform(1, 10))
        current_date = (datetime.strptime(current_date, '%m/%d/%Y %H:%M:%S') + timedelta(seconds=1)).strftime('%m/%d/%Y %H:%M:%S')
