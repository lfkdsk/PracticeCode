the_count = [1,2,3,4,5]
fruits = ['apple','oranges','peers','apricots']
change = [1,'pennies',2,'dimes',3,'quarters']

for number in the_count
  puts "The count is #{number}"
end

fruits.each do |fruit|
  puts "A fruit of type : #{fruit}"
end

change.each {|i| puts "I got #{i}"}

elements = []

(0..5).each do |i|
  puts "addint #{i} to the list"
  elements.push(i)
end

elements.each {|i| puts "Element was: #{i}"}
