filename = ARGV.first

puts "We are going to erase #{filename}"

puts "If you don't want that,hit CTRL-c "

puts "IF you do want that ,hit return"

$stdin.gets

puts "Opening the file ..."

target = open(filename,'w')

puts "Truncating the file ,GoodBye!"
target.truncate(0)

puts "Now I'm going to ask you for three line"

print "line 1:"
line1 = $stdin.gets.chomp

print "line 2:"
line2 = $stdin.gets.chomp

print "line 3:"
line3 = $stdin.gets.chomp

puts "I'm going to write thest to the file"

target.write(line1)
target.write("\n")
target.write(line2)
target.write("\n")
target.write(line3)
target.write("\n")

puts "And finally,we close it"
target.close
