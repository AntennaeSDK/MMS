
# replace the path to H2 jar appropriately
java -cp ~/.m2/repository/com/h2database/h2/1.4.187/h2-1.4.187.jar org.h2.tools.Shell -url "jdbc:h2:file:~/.mms/mms" -user sa -password "" -driver org.h2.Driver