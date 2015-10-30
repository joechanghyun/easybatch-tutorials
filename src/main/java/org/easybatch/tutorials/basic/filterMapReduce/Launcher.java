/*
 * The MIT License
 *
 *  Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package org.easybatch.tutorials.basic.filterMapReduce;

import org.easybatch.core.job.Job;
import org.easybatch.core.job.JobExecutor;
import org.easybatch.core.job.JobReport;
import org.easybatch.core.reader.IterableRecordReader;

import java.util.ArrayList;
import java.util.List;

import static org.easybatch.core.job.JobBuilder.aNewJob;

/**
 * Main class to run the filter-map-reduce tutorial.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class Launcher {

    public static void main(String[] args) throws Exception {

        List<Person> dataSource = new ArrayList<>();
        dataSource.add(new Person("jean", "france", 10));
        dataSource.add(new Person("foo", "usa", 30));
        dataSource.add(new Person("bar", "belgium", 20));
        dataSource.add(new Person("jacques", "france", 40));

        /*
         * Example 1: find the youngest french person's age from the list of persons
         */

        // Build a batch job
        Job job = aNewJob()
                .reader(new IterableRecordReader<>(dataSource))
                .filter(new CountryFilter("france"))
                .mapper(new AgeMapper())
                .processor(new MinCalculator())
                .build();

        // Run the job
        JobReport report = JobExecutor.execute(job);

        // Print the job execution report
        System.out.println("The youngest french person's age is: " + report.getResult());

        /*
         * Example 2: group persons by country
         */

        report = aNewJob()
                .reader(new IterableRecordReader<>(dataSource))
                .processor(new GroupByCountry())
                .call();

        System.out.println("Persons grouped by country: " + report.getResult());

    }

}
