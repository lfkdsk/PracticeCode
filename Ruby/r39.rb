states = {
  'Oregon' => 'OR',
  'Florida' => 'FL',
  'California' => 'CA',
  "NewYork" => 'NY',
  "Michigan" => 'MI'
}

cities = {
  'CA' => 'San Francisco',
  'MI' => 'Detroit',
  'FL' => 'Jacksonville'
}

cities['NY'] = 'NewYork'
cities['OR'] = 'Portland'

puts '-' * 10

puts "NA State has #{cities['NY']}"
puts "OR State has #{cities['OR']}"


puts '-' * 10

puts "Michigan has: #{cities[states['Michigan']]}"
puts "Florida has: #{cities[states['Florida']]}"

puts "-" * 10
states.each do |state,abbrev|
  puts "#{states} is abbreviated #{abbrev} "
end

puts '-' * 10
cities.each do |abbrev,city|
  puts "#{abbrev} has the city #{city}"
end

puts '-' * 10
states.each do |state,abbrev|
  city = cities[abbrev]
  puts "#{state} is abbreviated #{abbrev} and has city #{city}"
end

puts '-' * 10
state = states['Texas']

if !state
    puts "Sorry,no Texas"
end

city = cities['TX']
city ||= 'Does No Exist'
puts "The city for the state 'TX' is: #{city}"
